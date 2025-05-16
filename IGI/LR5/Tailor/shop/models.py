from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.utils.text import slugify
from django.core.validators import RegexValidator
from datetime import date

# Create your models here.

class Company(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.name
    
    class Meta:
        verbose_name_plural = "Информация о компании"

class Category(models.Model):
    name = models.CharField(max_length=100)
    slug = models.SlugField(unique=True)
    
    def __str__(self):
        return self.name
    
    class Meta:
        verbose_name_plural = "Категории"

class Article(models.Model):
    title = models.CharField(max_length=200)
    slug = models.SlugField(unique=True, default='', blank=True)
    content = models.TextField()
    summary = models.TextField(max_length=500)
    image = models.ImageField(upload_to='articles/', null=True, blank=True)
    category = models.ForeignKey(Category, on_delete=models.SET_NULL, null=True, blank=True, related_name='articles')
    author = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True)
    published_at = models.DateTimeField(default=timezone.now)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def save(self, *args, **kwargs):
        if not self.slug:
            self.slug = slugify(self.title)
        super().save(*args, **kwargs)

    def __str__(self):
        return self.title

    class Meta:
        verbose_name = "Новость"
        verbose_name_plural = "Новости"
        ordering = ['-published_at']

class Specialization(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField(blank=True)
    price = models.DecimalField(max_digits=10, decimal_places=2, default=0.0, verbose_name="Базовая цена")
    image = models.ImageField(upload_to='specializations/', null=True, blank=True)
    
    def __str__(self):
        return self.name
    
    class Meta:
        verbose_name = "Специализация"
        verbose_name_plural = "Специализации"

class Part(models.Model):
    name = models.CharField(max_length=100, verbose_name="Название запчасти")
    price = models.DecimalField(max_digits=10, decimal_places=2, default=0.0, verbose_name="Цена")
    specialization = models.ForeignKey(Specialization, on_delete=models.CASCADE, related_name='parts')
    
    def __str__(self):
        return f"{self.name} ({self.price} руб.)"
    
    class Meta:
        verbose_name = "Запчасть"
        verbose_name_plural = "Запчасти"

# Функция для проверки возраста 18+
def validate_18_years_or_older(birth_date):
    today = date.today()
    min_date = date(today.year - 18, today.month, today.day)
    if birth_date > min_date:
        from django.core.exceptions import ValidationError
        raise ValidationError('Вам должно быть не менее 18 лет')

class Master(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='master_profile')
    phone_number = models.CharField(
        max_length=20, 
        validators=[
            RegexValidator(
                regex=r'^\+375 \((?:29|33|44|25)\) \d{3}-\d{2}-\d{2}$',
                message="Phone number must be entered in the format: '+375 (29) XXX-XX-XX'"
            )
        ],
        null=True,
        blank=True
    )
    birth_date = models.DateField(validators=[validate_18_years_or_older])
    specializations = models.ManyToManyField(Specialization, related_name='masters')
    bio = models.TextField(blank=True)
    photo = models.ImageField(upload_to='masters/', null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.user.first_name} {self.user.last_name}"
    
    class Meta:
        verbose_name = "Мастер"
        verbose_name_plural = "Мастера"

class Client(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='client_profile')
    phone_number = models.CharField(
        max_length=20, 
        validators=[
            RegexValidator(
                regex=r'^\+375 \((?:29|33|44|25)\) \d{3}-\d{2}-\d{2}$',
                message="Phone number must be entered in the format: '+375 (29) XXX-XX-XX'"
            )
        ],
        null=True,
        blank=True
    )
    birth_date = models.DateField(validators=[validate_18_years_or_older])
    address = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.user.first_name} {self.user.last_name}"
    
    class Meta:
        verbose_name = "Клиент"
        verbose_name_plural = "Клиенты"

class Order(models.Model):
    STATUS_CHOICES = [
        ('pending', 'Ожидает подтверждения'),
        ('confirmed', 'Подтвержден'),
        ('in_progress', 'В работе'),
        ('completed', 'Выполнен'),
        ('cancelled', 'Отменен'),
    ]
    
    client = models.ForeignKey(Client, on_delete=models.CASCADE, related_name='orders')
    master = models.ForeignKey(Master, on_delete=models.CASCADE, related_name='orders')
    specialization = models.ForeignKey(Specialization, on_delete=models.CASCADE, related_name='orders')
    part = models.ForeignKey(Part, on_delete=models.SET_NULL, null=True, blank=True, related_name='orders')
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='pending')
    price = models.DecimalField(max_digits=10, decimal_places=2)
    description = models.TextField(blank=True, verbose_name='Описание заказа')
    scheduled_date = models.DateField(null=True, blank=True)
    scheduled_time = models.TimeField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"Заказ №{self.id} - {self.client} - {self.specialization}"
    
    class Meta:
        verbose_name = "Заказ"
        verbose_name_plural = "Заказы"
        ordering = ['-created_at']

class Review(models.Model):
    RATING_CHOICES = [
        (1, '1 - Очень плохо'),
        (2, '2 - Плохо'),
        (3, '3 - Нормально'),
        (4, '4 - Хорошо'),
        (5, '5 - Отлично'),
    ]
    
    client = models.ForeignKey(Client, on_delete=models.CASCADE, related_name='reviews')
    rating = models.IntegerField(choices=RATING_CHOICES, default=5)
    content = models.TextField(verbose_name='Текст отзыва')
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"Отзыв от {self.client.user.first_name} {self.client.user.last_name} - {self.get_rating_display()}"
    
    class Meta:
        verbose_name = "Отзыв"
        verbose_name_plural = "Отзывы"
        ordering = ['-created_at']

class Vacancy(models.Model):
    title = models.CharField(max_length=150, verbose_name="Название вакансии")
    specialization = models.OneToOneField(Specialization, on_delete=models.CASCADE, related_name='vacancy', verbose_name="Специализация")
    salary = models.DecimalField(max_digits=10, decimal_places=2, verbose_name="Зарплата в месяц (руб.)")
    description = models.TextField(verbose_name="Описание вакансии")
    is_active = models.BooleanField(default=True, verbose_name="Активна")
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.title} ({self.specialization.name}) - {self.salary} руб."
    
    class Meta:
        verbose_name = "Вакансия"
        verbose_name_plural = "Вакансии"
        ordering = ['-created_at']

class Promocode(models.Model):
    DISCOUNT_TYPE_CHOICES = [
        ('percentage', 'Процент'),
        ('fixed', 'Фиксированная сумма'),
    ]
    
    code = models.CharField(max_length=20, unique=True, verbose_name="Код промокода")
    discount_type = models.CharField(max_length=10, choices=DISCOUNT_TYPE_CHOICES, default='percentage', verbose_name="Тип скидки")
    discount_amount = models.DecimalField(max_digits=10, decimal_places=2, verbose_name="Размер скидки")
    specializations = models.ManyToManyField(Specialization, related_name='promocodes', verbose_name="Специализации")
    valid_from = models.DateField(verbose_name="Действует с")
    valid_to = models.DateField(verbose_name="Действует до")
    is_active = models.BooleanField(default=True, verbose_name="Активен")
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def is_valid(self):
        """Check if promocode is currently valid"""
        today = date.today()
        return (
            self.is_active and 
            self.valid_from <= today <= self.valid_to
        )
    
    def calculate_discount(self, price):
        """Calculate discount amount based on price and discount type"""
        if not self.is_valid():
            return 0
            
        if self.discount_type == 'percentage':
            # Ensure percentage is not more than 100%
            percentage = min(self.discount_amount, 100)
            return (price * percentage) / 100
        else:
            # For fixed discount, don't give more than the price
            return min(self.discount_amount, price)
    
    def __str__(self):
        if self.discount_type == 'percentage':
            return f"{self.code} ({self.discount_amount}%)"
        else:
            return f"{self.code} ({self.discount_amount} руб.)"
    
    class Meta:
        verbose_name = "Промокод"
        verbose_name_plural = "Промокоды"
        ordering = ['-valid_to']

class GlossaryTerm(models.Model):
    term = models.CharField(max_length=100, unique=True, verbose_name="Термин")
    definition = models.TextField(verbose_name="Определение")
    added_date = models.DateField(default=date.today, verbose_name="Дата добавления")
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return self.term
    
    class Meta:
        verbose_name = "Термин"
        verbose_name_plural = "Термины"
        ordering = ['term']
