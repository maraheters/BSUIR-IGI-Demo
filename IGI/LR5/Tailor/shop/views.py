from django.shortcuts import render, redirect, get_object_or_404
from django.utils import timezone
from datetime import timezone as dt_timezone
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth import login, logout, authenticate
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from .models import Company, Article, Category, Master, Client, Specialization, Order, Review, Part, Vacancy, Promocode, GlossaryTerm
from .forms import UserRegistrationForm, ClientRegistrationForm, MasterRegistrationForm, ReviewForm, VacancyForm
from django.contrib.admin.views.decorators import staff_member_required
from django.db.models import Avg, Count, Sum
from django.db.models.functions import TruncDate
from statistics import mode, median
import json
import calendar
from datetime import date
import requests
import os
from django.conf import settings

import logging
logger = logging.getLogger(__name__)

# Helper functions

def get_time_context(request=None):
    """Helper function to get timezone context used in all views
    """
    now = timezone.now()
    utc_time = now.astimezone(dt_timezone.utc)
    
    local_time = now
    
    # Try to get user timezone from request cookies or session if request is provided
    user_timezone = None
    if request and 'user_timezone' in request.COOKIES:
        try:
            user_timezone = request.COOKIES['user_timezone']

            from pytz import timezone as pytz_timezone
            local_time = utc_time.astimezone(pytz_timezone(user_timezone))

        except Exception as e:
            logger.warning(f"Failed to parse user timezone: {e}")

    return {
        'local_time_str': local_time.strftime('%d/%m/%Y %H:%M'),
        'utc_time_str': utc_time.strftime('%d/%m/%Y %H:%M'),
        'user_timezone': user_timezone or 'UTC',
    }

def render_with_time(request, template, context_data=None):
    """Helper function to render a template with time context data"""
    context = get_time_context(request)
    if context_data:
        context.update(context_data)
    return render(request, template, context)

def safe_query(query_func, error_message, default_return=None):
    """Helper function to safely execute database queries with error handling"""
    try:
        return query_func()
    except Exception as e:
        logger.error(f"{error_message}: {e}")
        return default_return

# Views

def home(request):
    """View for the home page that displays the latest article"""
    latest_article = safe_query(
        lambda: Article.objects.order_by('-published_at').first(),
        "Error fetching latest article"
    )
    
    return render_with_time(
        request, 
        'shop/home.html', 
        {'latest_article': latest_article}
    )

def about(request):
    """View for the about company page"""
    company_info = safe_query(
        lambda: Company.objects.first(),
        "Error fetching company info"
    )
    
    return render_with_time(
        request, 
        'shop/about.html', 
        {'company_info': company_info}
    )

def contacts(request):
    """View for the contacts page"""
    masters = safe_query(
        lambda: Master.objects.all(),
        "Error fetching masters"
    )
    
    return render_with_time(
        request, 
        'shop/contacts.html', 
        {'masters': masters}
    )

def services_list(request):
    """View for the services list page"""
    search_query = request.GET.get('search', '')
    sort_by = request.GET.get('sort_by', '')
    
    services = safe_query(
        lambda: Specialization.objects.filter(name__icontains=search_query) if search_query else Specialization.objects.all(),
        "Error fetching services"
    )
    
    # Apply sorting if requested
    if sort_by == 'price_asc':
        services = services.order_by('price')
    elif sort_by == 'price_desc':
        services = services.order_by('-price')
    elif sort_by == 'name_asc':
        services = services.order_by('name')
    elif sort_by == 'name_desc':
        services = services.order_by('-name')
    
    return render_with_time(
        request, 
        'shop/services_list.html', 
        {
            'services': services,
            'search_query': search_query,
            'sort_by': sort_by
        }
    )

@login_required
def service_masters(request, service_id):
    """View for selecting a master for a specific service"""
    service = get_object_or_404(Specialization, id=service_id)
    
    masters = safe_query(
        lambda: Master.objects.filter(specializations=service),
        f"Error fetching masters for service {service_id}",
        []
    )
    
    return render_with_time(
        request, 
        'shop/service_masters.html', 
        {
            'service': service,
            'masters': masters
        }
    )

@login_required
def create_order(request, service_id, master_id):
    """View for creating a new order with selected service and master"""
    service = get_object_or_404(Specialization, id=service_id)
    master = get_object_or_404(Master, id=master_id)
    
    # Получаем запчасти для данной специализации
    parts = Part.objects.filter(specialization=service)
    
    # Проверяем, что у пользователя есть профиль клиента
    if not hasattr(request.user, 'client_profile'):
        messages.error(request, 'Для создания заказа вам необходим профиль клиента')
        return redirect('shop:profile')
    
    client = request.user.client_profile
    
    # Для хранения информации о промокоде
    promocode_info = {
        'applied': False,
        'discount': 0,
        'error': None
    }
    
    if request.method == 'POST':
        description = request.POST.get('description', '')
        part_id = request.POST.get('part')
        scheduled_date = request.POST.get('scheduled_date')
        scheduled_time = request.POST.get('scheduled_time')
        promocode = request.POST.get('promocode', '').strip()
        
        try:
            # Рассчитываем базовую цену услуги
            service_price = service.price
            part = None
            part_price = 0
            
            if part_id:
                try:
                    part = Part.objects.get(id=part_id, specialization=service)
                    part_price = part.price
                except Part.DoesNotExist:
                    pass
            
            # Проверяем промокод
            applied_promocode = None
            discount_amount = 0
            
            if promocode:
                try:
                    promo = Promocode.objects.get(code=promocode, is_active=True)
                    
                    # Проверяем, подходит ли промокод для данной специализации
                    if not promo.specializations.filter(id=service.id).exists():
                        promocode_info['error'] = "Этот промокод не применим к выбранной услуге"
                    elif not promo.is_valid():
                        promocode_info['error'] = "Срок действия промокода истек"
                    else:
                        # Применяем скидку только к стоимости услуги, не к запчастям
                        discount_amount = promo.calculate_discount(service_price)
                        applied_promocode = promo
                        
                        promocode_info['applied'] = True
                        promocode_info['discount'] = discount_amount
                        
                        logger.info(f"Promocode {promocode} applied with discount {discount_amount}")
                
                except Promocode.DoesNotExist:
                    promocode_info['error'] = "Указанный промокод не найден"
                    logger.info(f"Invalid promocode attempt: {promocode}")
            
            # Финальная цена с учетом скидки
            final_service_price = max(service_price - discount_amount, 0)
            total_price = final_service_price + part_price
            
            # Создаем новый заказ
            order = Order.objects.create(
                client=client,
                master=master,
                specialization=service,
                part=part,
                status='pending',
                price=total_price,
                description=description,
                scheduled_date=scheduled_date,
                scheduled_time=scheduled_time
            )
            
            # Добавляем информацию о примененном промокоде в сообщение
            success_message = f'Заказ №{order.id} успешно создан'
            if applied_promocode:
                success_message += f'. Применен промокод {applied_promocode.code} со скидкой {discount_amount:.2f} руб.'
            
            messages.success(request, success_message)
            logger.info(f"New order created: #{order.id} by client {client.user.username}")
            
            # Перенаправляем на личный кабинет пользователя
            return redirect('shop:profile')
        
        except Exception as e:
            logger.error(f"Error creating order: {e}")
            messages.error(request, 'Произошла ошибка при создании заказа')
    
    # Получаем активные промокоды для этой специализации (для отображения в шаблоне)
    active_promocodes = safe_query(
        lambda: Promocode.objects.filter(
            specializations=service,
            is_active=True,
            valid_from__lte=date.today(),
            valid_to__gte=date.today()
        ),
        f"Error fetching promocodes for service {service_id}",
        []
    )
    
    return render_with_time(
        request, 
        'shop/create_order.html', 
        {
            'service': service,
            'master': master,
            'parts': parts,
            'active_promocodes': active_promocodes,
            'promocode_info': promocode_info
        }
    )

@login_required
def update_order_status(request, order_id, new_status):
    """View for updating order status"""
    # Проверяем, что у пользователя есть профиль мастера
    if not hasattr(request.user, 'master_profile'):
        messages.error(request, 'Только мастера могут изменять статус заказа')
        return redirect('shop:profile')
    
    order = get_object_or_404(Order, id=order_id)
    
    # Проверяем, что заказ принадлежит текущему мастеру
    if order.master != request.user.master_profile:
        messages.error(request, 'Вы можете изменять статус только своих заказов')
        logger.warning(f"Unauthorized attempt to change order status: {request.user.username} tried to modify order {order_id}")
        return redirect('shop:profile')
    
    # Проверяем валидность нового статуса
    valid_statuses = [status[0] for status in Order.STATUS_CHOICES]
    if new_status not in valid_statuses:
        messages.error(request, 'Некорректный статус заказа')
        return redirect('shop:profile')
    
    try:
        # Обновляем статус заказа
        order.status = new_status
        order.save()
        
        status_display = dict(Order.STATUS_CHOICES)[new_status]
        messages.success(request, f'Статус заказа №{order.id} изменен на "{status_display}"')
        logger.info(f"Order #{order.id} status changed to {new_status} by {request.user.username}")
    except Exception as e:
        logger.error(f"Error updating order status: {e}")
        messages.error(request, 'Произошла ошибка при изменении статуса заказа')
    
    return redirect('shop:profile')

def news_list(request):
    """View for the news list page"""
    categories = Category.objects.all()
    selected_category = request.GET.get('category')
    
    articles = safe_query(
        lambda: Article.objects.filter(category__slug=selected_category) if selected_category else Article.objects.all(),
        "Error fetching articles"
    )
    
    # Fetch fashion news from News API
    fashion_news = []
    try:
        news_api_key = settings.NEWS_API_KEY
        
        if news_api_key and news_api_key != "your_news_api_key_here":
            response = requests.get(
                f"https://newsapi.org/v2/everything?q=fashion&apiKey={news_api_key}"
            )
            
            if response.status_code == 200:
                fashion_data = response.json()
                # Get up to 6 articles
                fashion_news = fashion_data.get('articles', [])[:6]
                logger.info(f"Successfully fetched {len(fashion_news)} fashion news articles")
            else:
                logger.error(f"Failed to fetch fashion news: {response.status_code}")
        else:
            logger.error("NEWS_API_KEY not properly configured in settings.py")
    except Exception as e:
        logger.error(f"Error fetching fashion news: {e}")
    
    return render_with_time(
        request, 
        'shop/news_list.html', 
        {
            'articles': articles,
            'categories': categories,
            'selected_category': selected_category,
            'fashion_news': fashion_news
        }
    )

def article_detail(request, slug):
    """View for the article detail page"""
    article = get_object_or_404(Article, slug=slug)
    
    return render_with_time(
        request, 
        'shop/article_detail.html', 
        {'article': article}
    )

def register(request):
    """View for user registration selection"""
    return render_with_time(request, 'shop/register_selection.html')

def process_registration(request, user_form_class, profile_form_class, success_message, profile_type):
    """Helper function to process registration for both client and master"""
    if request.method == 'POST':
        user_form = user_form_class(request.POST)
        profile_form = profile_form_class(request.POST, request.FILES if profile_type == 'master' else request.POST)
        
        if user_form.is_valid() and profile_form.is_valid():
            user = user_form.save()
            
            # Create profile but don't save to database yet
            profile = profile_form.save(commit=False)
            profile.user = user  # Link profile to user
            profile.save()
            
            # Save many-to-many relationships if master
            if profile_type == 'master':
                profile_form.save_m2m()
            
            # Log the user in
            login(request, user)
            messages.success(request, success_message)
            logger.info(f"New {profile_type} registered: {user.username}")
            return redirect('shop:home')
        else:
            # Log form validation errors
            logger.warning(f"{profile_type.capitalize()} registration form errors: {user_form.errors} {profile_form.errors}")
            for form in [user_form, profile_form]:
                for field, errors in form.errors.items():
                    for error in errors:
                        messages.error(request, f"{field}: {error}")
    else:
        user_form = user_form_class()
        profile_form = profile_form_class()
    
    return render_with_time(
        request, 
        'shop/register_profile.html', 
        {
            'user_form': user_form,
            'profile_form': profile_form,
            'user_type': profile_type
        }
    )

def register_client(request):
    """View for client registration"""
    return process_registration(
        request,
        UserRegistrationForm,
        ClientRegistrationForm,
        'Регистрация клиента успешна!',
        'client'
    )

def register_master(request):
    """View for master registration"""
    return process_registration(
        request,
        UserRegistrationForm,
        MasterRegistrationForm,
        'Регистрация мастера успешна!',
        'master'
    )

@login_required
def profile(request):
    """View for user profile"""
    user = request.user
    is_master = hasattr(user, 'master_profile')
    is_client = hasattr(user, 'client_profile')
    
    # Generate text calendar
    cal = calendar.TextCalendar(firstweekday=0)
    current_date = timezone.now().date()
    text_calendar = cal.formatmonth(current_date.year, current_date.month)
    
    context = get_time_context(request)
    context.update({
        'user': user,
        'is_master': is_master,
        'is_client': is_client,
        'text_calendar': text_calendar,
    })
    
    # Only fetch a joke if the user has a profile
    if is_master or is_client:
        # Fetch a random joke from the API
        joke = None
        try:
            response = requests.get("https://official-joke-api.appspot.com/random_joke")
            if response.status_code == 200:
                joke = response.json()
                logger.info("Successfully fetched a joke from the API")
            else:
                logger.error(f"Failed to fetch joke: {response.status_code}")
        except Exception as e:
            logger.error(f"Error fetching joke: {e}")
        
        context['joke'] = joke
    
    if is_master:
        # Получаем заказы мастера
        orders = safe_query(
            lambda: Order.objects.filter(master=user.master_profile),
            f"Error fetching orders for master {user.username}"
        )
        if orders is not None:
            context['orders'] = orders
        
        context['profile'] = user.master_profile
        return render(request, 'shop/master_profile.html', context)
    elif is_client:
        # Получаем заказы клиента
        orders = safe_query(
            lambda: Order.objects.filter(client=user.client_profile),
            f"Error fetching orders for client {user.username}"
        )
        if orders is not None:
            context['orders'] = orders
        
        context['profile'] = user.client_profile
        return render(request, 'shop/client_profile.html', context)
    else:
        messages.warning(request, 'У вас нет профиля мастера или клиента. Пожалуйста, создайте профиль.')
        return redirect('shop:register')

def login_view(request):
    """View for user login"""
    if request.method == 'POST':
        form = AuthenticationForm(request, data=request.POST)
        if form.is_valid():
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password')
            user = authenticate(username=username, password=password)
            if user is not None:
                login(request, user)
                messages.success(request, f'Добро пожаловать, {username}!')
                return redirect('shop:home')
        else:
            messages.error(request, 'Неверное имя пользователя или пароль')
    else:
        form = AuthenticationForm()
    
    return render_with_time(
        request, 
        'shop/login.html', 
        {'form': form}
    )

@login_required
def logout_view(request):
    """View for user logout"""
    logout(request)
    messages.info(request, 'Вы вышли из системы')
    return redirect('shop:home')

def reviews_list(request):
    """View for the reviews list page"""
    reviews = safe_query(
        lambda: Review.objects.select_related('client__user').all(),
        "Error fetching reviews",
        []
    )
    
    return render_with_time(
        request, 
        'shop/reviews_list.html', 
        {'reviews': reviews}
    )

@login_required
def add_review(request):
    """View for adding a new review"""
    # Проверяем, что у пользователя есть профиль клиента
    if not hasattr(request.user, 'client_profile'):
        messages.error(request, 'Для добавления отзыва вам необходим профиль клиента')
        return redirect('shop:register_client')
    
    client = request.user.client_profile
    
    if request.method == 'POST':
        form = ReviewForm(request.POST)
        if form.is_valid():
            try:
                review = form.save(commit=False)
                review.client = client
                review.save()
                
                messages.success(request, 'Спасибо! Ваш отзыв успешно опубликован.')
                logger.info(f"New review added by client {client.user.username}")
                return redirect('shop:reviews_list')
            except Exception as e:
                logger.error(f"Error adding review: {e}")
                messages.error(request, 'Произошла ошибка при добавлении отзыва')
    else:
        form = ReviewForm()
    
    return render_with_time(
        request, 
        'shop/add_review.html', 
        {'form': form}
    )

@staff_member_required
def statistics(request):
    """View for statistics page, only accessible to superusers"""
    logger.info(f"Statistics page accessed by {request.user.username}")
    
    # Clients in alphabetical order with sum of orders
    clients = Client.objects.all().order_by('user__first_name', 'user__last_name')
    
    # Calculate total sum for each client
    for client in clients:
        client.total_orders_sum = client.orders.aggregate(Sum('price'))['price__sum'] or 0
    
    # Orders statistics
    total_orders = Order.objects.count()
    total_revenue = Order.objects.aggregate(Sum('price'))['price__sum'] or 0
    
    # Average, median order price
    all_prices = list(Order.objects.values_list('price', flat=True))
    avg_order_price = Order.objects.aggregate(Avg('price'))['price__avg'] or 0
    
    # Calculate median order price
    median_order_price = 0
    if all_prices:
        median_order_price = median(all_prices)
    
    # Most popular specialization
    popular_specialization = Specialization.objects.annotate(
        order_count=Count('orders')
    ).order_by('-order_count').first()
    
    # Most profitable specialization
    profitable_specialization = Specialization.objects.annotate(
        total_revenue=Sum('orders__price')
    ).order_by('-total_revenue').first()
    
    # Client age statistics
    client_ages = []
    for client in Client.objects.all():
        today = date.today()
        age = today.year - client.birth_date.year - ((today.month, today.day) < (client.birth_date.month, client.birth_date.day))
        client_ages.append(age)
    
    avg_client_age = 0
    median_client_age = 0
    if client_ages:
        avg_client_age = sum(client_ages) / len(client_ages)
        median_client_age = median(client_ages)
    
    # Orders by date - using date trunc to get the date part
    orders_by_date = Order.objects.annotate(
        date=TruncDate('created_at')
    ).values('date').annotate(count=Count('id')).order_by('date')
    
    # Format dates for JSON
    dates = []
    order_counts = []
    for entry in orders_by_date:
        if entry['date']:
            date_str = entry['date'].strftime('%d/%m/%Y')
            dates.append(date_str)
            order_counts.append(entry['count'])
    
    # Specialization distribution
    specialization_stats = Specialization.objects.annotate(
        order_count=Count('orders')
    ).values('name', 'order_count').order_by('-order_count')
    
    spec_names = [entry['name'] for entry in specialization_stats]
    spec_counts = [entry['order_count'] for entry in specialization_stats]
    
    context = get_time_context(request)
    context.update({
        'clients': clients,
        'total_orders': total_orders,
        'total_revenue': total_revenue,
        'avg_order_price': avg_order_price,
        'median_order_price': median_order_price,
        'popular_specialization': popular_specialization,
        'profitable_specialization': profitable_specialization,
        'avg_client_age': avg_client_age,
        'median_client_age': median_client_age,
        'dates_json': json.dumps(dates),
        'order_counts_json': json.dumps(order_counts),
        'spec_names_json': json.dumps(spec_names),
        'spec_counts_json': json.dumps(spec_counts),
    })
    
    return render(request, 'shop/statistics.html', context)

def privacy_policy(request):
    """View for the privacy policy page"""
    return render_with_time(request, 'shop/privacy_policy.html')

def vacancies_list(request):
    """View for displaying available job vacancies"""
    # Get optional filters
    specialization_id = request.GET.get('specialization')
    min_salary = request.GET.get('min_salary')
    
    # Start with all active vacancies for regular users, all vacancies for staff/admin
    if request.user.is_staff:
        vacancies = safe_query(
            lambda: Vacancy.objects.all(),
            "Error fetching vacancies"
        )
    else:
        vacancies = safe_query(
            lambda: Vacancy.objects.filter(is_active=True),
            "Error fetching vacancies"
        )
    
    # Apply filters if provided
    if specialization_id:
        try:
            vacancies = vacancies.filter(specialization_id=int(specialization_id))
        except (ValueError, TypeError):
            pass
            
    if min_salary:
        try:
            vacancies = vacancies.filter(salary__gte=float(min_salary))
        except (ValueError, TypeError):
            pass
    
    # Get all specializations for filter dropdown
    specializations = safe_query(
        lambda: Specialization.objects.all(),
        "Error fetching specializations"
    )
    
    # For admin/staff, provide a form to create new vacancy
    vacancy_form = None
    if request.user.is_staff and request.method == 'POST':
        vacancy_form = VacancyForm(request.POST)
        if vacancy_form.is_valid():
            vacancy_form.save()
            messages.success(request, "Вакансия успешно создана!")
            return redirect('shop:vacancies_list')
    elif request.user.is_staff:
        vacancy_form = VacancyForm()
    
    return render_with_time(
        request, 
        'shop/vacancies_list.html', 
        {
            'vacancies': vacancies,
            'specializations': specializations,
            'selected_specialization': specialization_id,
            'min_salary': min_salary,
            'vacancy_form': vacancy_form
        }
    )

@staff_member_required
def vacancy_edit(request, vacancy_id):
    """View for editing a vacancy"""
    vacancy = get_object_or_404(Vacancy, id=vacancy_id)
    
    if request.method == 'POST':
        form = VacancyForm(request.POST, instance=vacancy)
        if form.is_valid():
            form.save()
            messages.success(request, f"Вакансия '{vacancy.title}' успешно обновлена!")
            return redirect('shop:vacancies_list')
    else:
        form = VacancyForm(instance=vacancy)
    
    return render_with_time(
        request,
        'shop/vacancy_edit.html',
        {
            'form': form,
            'vacancy': vacancy
        }
    )

@staff_member_required
def vacancy_delete(request, vacancy_id):
    """View for deleting a vacancy"""
    vacancy = get_object_or_404(Vacancy, id=vacancy_id)
    
    if request.method == 'POST':
        vacancy_title = vacancy.title
        vacancy.delete()
        messages.success(request, f"Вакансия '{vacancy_title}' успешно удалена!")
        return redirect('shop:vacancies_list')
    
    return render_with_time(
        request,
        'shop/vacancy_confirm_delete.html',
        {'vacancy': vacancy}
    )

def promocodes_list(request):
    """View for displaying promocodes"""
    today = date.today()
    
    # Get active promocodes
    active_promocodes = safe_query(
        lambda: Promocode.objects.filter(
            is_active=True,
            valid_from__lte=today,
            valid_to__gte=today
        ),
        "Error fetching active promocodes",
        []
    )
    
    # Get expired/archived promocodes
    archived_promocodes = safe_query(
        lambda: Promocode.objects.filter(
            valid_to__lt=today
        ) | Promocode.objects.filter(
            is_active=False
        ),
        "Error fetching archived promocodes",
        []
    )
    
    return render_with_time(
        request, 
        'shop/promocodes_list.html', 
        {
            'active_promocodes': active_promocodes,
            'archived_promocodes': archived_promocodes
        }
    )

def glossary_list(request):
    """View for displaying glossary terms and concepts"""
    # Get search query if any
    search_query = request.GET.get('search', '')
    
    # Filter terms based on search
    terms = safe_query(
        lambda: GlossaryTerm.objects.filter(term__icontains=search_query) if search_query else GlossaryTerm.objects.all().order_by('term'),
        "Error fetching glossary terms",
        []
    )
    
    return render_with_time(
        request, 
        'shop/glossary_list.html', 
        {
            'terms': terms,
            'search_query': search_query,
        }
    )
