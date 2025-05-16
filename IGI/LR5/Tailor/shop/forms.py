from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from django.core.validators import RegexValidator
from django.contrib.auth.password_validation import validate_password
from .models import Master, Client, Specialization, Review, Vacancy
from django.utils import timezone
from datetime import date

class UserRegistrationForm(UserCreationForm):
    email = forms.EmailField(required=True, help_text='Требуется действующий email адрес')
    first_name = forms.CharField(max_length=30, required=True, help_text='Обязательное поле')
    last_name = forms.CharField(max_length=30, required=True, help_text='Обязательное поле')
    
    class Meta:
        model = User
        fields = ('username', 'first_name', 'last_name', 'email', 'password1', 'password2')

    def clean_email(self):
        email = self.cleaned_data.get('email')
        if User.objects.filter(email=email).exists():
            raise forms.ValidationError('Пользователь с таким email уже существует')
        return email
    
    def save(self, commit=True):
        user = super(UserRegistrationForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
        user.first_name = self.cleaned_data['first_name']
        user.last_name = self.cleaned_data['last_name']
        if commit:
            user.save()
        return user

class DateInput(forms.DateInput):
    input_type = 'date'
    format = '%d/%m/%Y'

class ClientRegistrationForm(forms.ModelForm):
    phone_regex = RegexValidator(
        regex=r'^\+375 \((?:29|33|44|25)\) \d{3}-\d{2}-\d{2}$',
        message="Номер телефона должен быть в формате: '+375 (29) XXX-XX-XX'"
    )
    phone_number = forms.CharField(
        validators=[phone_regex], 
        max_length=20,
        help_text='Формат: +375 (29) XXX-XX-XX',
        required=False
    )
    birth_date = forms.DateField(
        widget=DateInput(),
        help_text='Вам должно быть не менее 18 лет',
        input_formats=['%d/%m/%Y', '%Y-%m-%d', '%m/%d/%Y']
    )
    
    class Meta:
        model = Client
        fields = ('phone_number', 'birth_date', 'address')
        widgets = {
            'address': forms.Textarea(attrs={'rows': 3}),
        }
    
    def clean_birth_date(self):
        birth_date = self.cleaned_data.get('birth_date')
        if not birth_date:
            return birth_date
            
        today = date.today()
        min_date = date(today.year - 18, today.month, today.day)
        
        if birth_date > min_date:
            raise forms.ValidationError('Вам должно быть не менее 18 лет')
        return birth_date

class MasterRegistrationForm(forms.ModelForm):
    phone_regex = RegexValidator(
        regex=r'^\+375 \((?:29|33|44|25)\) \d{3}-\d{2}-\d{2}$',
        message="Номер телефона должен быть в формате: '+375 (29) XXX-XX-XX'"
    )
    phone_number = forms.CharField(
        validators=[phone_regex], 
        max_length=20,
        help_text='Формат: +375 (29) XXX-XX-XX',
        required=False
    )
    birth_date = forms.DateField(
        widget=DateInput(),
        help_text='Вам должно быть не менее 18 лет',
        input_formats=['%d/%m/%Y', '%Y-%m-%d', '%m/%d/%Y']
    )
    specializations = forms.ModelMultipleChoiceField(
        queryset=Specialization.objects.all(),
        widget=forms.CheckboxSelectMultiple,
        required=True
    )
    
    class Meta:
        model = Master
        fields = ('phone_number', 'birth_date', 'specializations', 'bio', 'photo')
        widgets = {
            'bio': forms.Textarea(attrs={'rows': 4}),
        }
    
    def clean_birth_date(self):
        birth_date = self.cleaned_data.get('birth_date')
        if not birth_date:
            return birth_date
            
        today = date.today()
        min_date = date(today.year - 18, today.month, today.day)
        
        if birth_date > min_date:
            raise forms.ValidationError('Вам должно быть не менее 18 лет')
        return birth_date 

class ReviewForm(forms.ModelForm):
    class Meta:
        model = Review
        fields = ['rating', 'content']
        widgets = {
            'rating': forms.Select(attrs={'class': 'form-select'}),
            'content': forms.Textarea(attrs={'rows': 4, 'class': 'form-control', 'placeholder': 'Поделитесь своим мнением...'}),
        }
        labels = {
            'rating': 'Оценка',
            'content': 'Текст отзыва',
        } 

class VacancyForm(forms.ModelForm):
    class Meta:
        model = Vacancy
        fields = ['title', 'specialization', 'salary', 'description', 'is_active']
        widgets = {
            'title': forms.TextInput(attrs={'class': 'form-control', 'placeholder': 'Название вакансии'}),
            'specialization': forms.Select(attrs={'class': 'form-select'}),
            'salary': forms.NumberInput(attrs={'class': 'form-control', 'placeholder': 'Зарплата в месяц (руб.)'}),
            'description': forms.Textarea(attrs={'class': 'form-control', 'rows': 4, 'placeholder': 'Описание вакансии и требований к кандидатам'}),
            'is_active': forms.CheckboxInput(attrs={'class': 'form-check-input'}),
        }
        labels = {
            'title': 'Название вакансии',
            'specialization': 'Специализация',
            'salary': 'Зарплата в месяц (руб.)',
            'description': 'Описание вакансии',
            'is_active': 'Активна',
        } 