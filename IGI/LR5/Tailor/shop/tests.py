from django.test import TestCase, Client
from django.urls import reverse
from django.utils import timezone
from .models import Company, Article
import datetime

# Create your tests here.

class ModelsTestCase(TestCase):
    def setUp(self):
        # Create test data
        self.company = Company.objects.create(
            name="Тестовое ателье",
            description="Описание тестового ателье"
        )
        
        self.article = Article.objects.create(
            title="Тестовая статья",
            content="Полное содержание тестовой статьи",
            summary="Краткое содержание тестовой статьи",
            published_at=timezone.now()
        )
    
    def test_company_model(self):
        company = Company.objects.get(id=self.company.id)
        self.assertEqual(company.name, "Тестовое ателье")
        self.assertEqual(company.description, "Описание тестового ателье")
        self.assertEqual(str(company), "Тестовое ателье")
    
    def test_article_model(self):
        article = Article.objects.get(id=self.article.id)
        self.assertEqual(article.title, "Тестовая статья")
        self.assertEqual(article.content, "Полное содержание тестовой статьи")
        self.assertEqual(article.summary, "Краткое содержание тестовой статьи")
        self.assertEqual(str(article), "Тестовая статья")

class ViewsTestCase(TestCase):
    def setUp(self):
        # Create test client
        self.client = Client()
        
        # Create test data
        self.company = Company.objects.create(
            name="Тестовое ателье",
            description="Описание тестового ателье"
        )
        
        self.article = Article.objects.create(
            title="Тестовая статья",
            content="Полное содержание тестовой статьи",
            summary="Краткое содержание тестовой статьи",
            published_at=timezone.now()
        )
    
    def test_home_view(self):
        response = self.client.get(reverse('shop:home'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'shop/home.html')
        self.assertContains(response, "Тестовая статья")
        self.assertContains(response, "Краткое содержание тестовой статьи")
    
    def test_about_view(self):
        response = self.client.get(reverse('shop:about'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'shop/about.html')
        self.assertContains(response, "Тестовое ателье")
        self.assertContains(response, "Описание тестового ателье")
    
    def test_home_view_no_article(self):
        # Delete all articles
        Article.objects.all().delete()
        
        response = self.client.get(reverse('shop:home'))
        self.assertEqual(response.status_code, 200)
        self.assertContains(response, "На данный момент новостей нет.")
    
    def test_about_view_no_company(self):
        # Delete all company info
        Company.objects.all().delete()
        
        response = self.client.get(reverse('shop:about'))
        self.assertEqual(response.status_code, 200)
        self.assertContains(response, "Информация о компании еще не добавлена.")

class URLTestCase(TestCase):
    def setUp(self):
        self.client = Client()
    
    def test_home_url(self):
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
    
    def test_about_url(self):
        response = self.client.get('/about/')
        self.assertEqual(response.status_code, 200)
