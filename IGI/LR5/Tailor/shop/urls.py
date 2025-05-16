from django.urls import path, re_path
from . import views

app_name = 'shop'

urlpatterns = [
    re_path(r'^$', views.home, name='home'),
    re_path(r'^about/$', views.about, name='about'),
    re_path(r'^contacts/$', views.contacts, name='contacts'),

    re_path(r'^services/$', views.services_list, name='services_list'),
    re_path(r'^services/(?P<service_id>\d+)/masters/$', views.service_masters, name='service_masters'),
    re_path(r'^services/(?P<service_id>\d+)/masters/(?P<master_id>\d+)/order/$', views.create_order, name='create_order'),

    re_path(r'^news/$', views.news_list, name='news_list'),
    re_path(r'^news/(?P<slug>[\w-]+)/$', views.article_detail, name='article_detail'),

    re_path(r'^vacancies/$', views.vacancies_list, name='vacancies_list'),
    re_path(r'^vacancies/(?P<vacancy_id>\d+)/edit/$', views.vacancy_edit, name='vacancy_edit'),
    re_path(r'^vacancies/(?P<vacancy_id>\d+)/delete/$', views.vacancy_delete, name='vacancy_delete'),

    re_path(r'^promocodes/$', views.promocodes_list, name='promocodes_list'),

    re_path(r'^glossary/$', views.glossary_list, name='glossary_list'),

    re_path(r'^accounts/register/$', views.register, name='register'),
    re_path(r'^accounts/register/client/$', views.register_client, name='register_client'),
    re_path(r'^accounts/register/master/$', views.register_master, name='register_master'),
    re_path(r'^accounts/profile/$', views.profile, name='profile'),
    re_path(r'^accounts/login/$', views.login_view, name='login'),
    re_path(r'^accounts/logout/$', views.logout_view, name='logout'),

    re_path(r'^orders/(?P<order_id>\d+)/status/(?P<new_status>\w+)/$', views.update_order_status, name='update_order_status'),

    re_path(r'^reviews/$', views.reviews_list, name='reviews_list'),
    
    re_path(r'^reviews/add/$', views.add_review, name='add_review'),
    
    re_path(r'^statistics/$', views.statistics, name='statistics'),
    
    re_path(r'^privacy-policy/$', views.privacy_policy, name='privacy_policy'),
] 