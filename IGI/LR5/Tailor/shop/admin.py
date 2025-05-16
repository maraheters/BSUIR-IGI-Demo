from django.contrib import admin
from django.contrib.auth.models import User
from django.contrib.auth.admin import UserAdmin
from .models import Company, Article, Category, Specialization, Master, Client, Order, Review, Part, Vacancy, Promocode, GlossaryTerm

# Регистрируем модель User с настройками поиска для autocomplete
admin.site.unregister(User)
@admin.register(User)
class CustomUserAdmin(UserAdmin):
    search_fields = ['username']

@admin.register(Company)
class CompanyAdmin(admin.ModelAdmin):
    list_display = ('name', 'created_at', 'updated_at')
    search_fields = ('name', 'description')

@admin.register(Category)
class CategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'slug')
    prepopulated_fields = {'slug': ('name',)}
    search_fields = ('name',)

@admin.register(Article)
class ArticleAdmin(admin.ModelAdmin):
    list_display = ('title', 'category', 'author', 'published_at', 'created_at', 'updated_at')
    list_filter = ('published_at', 'category', 'author')
    search_fields = ('title', 'content', 'summary')
    prepopulated_fields = {'slug': ('title',)}
    date_hierarchy = 'published_at'
    autocomplete_fields = ('author',)

class PartInline(admin.TabularInline):
    model = Part
    extra = 1

@admin.register(Specialization)
class SpecializationAdmin(admin.ModelAdmin):
    list_display = ('name', 'price')
    search_fields = ('name', 'description')
    inlines = [PartInline]

@admin.register(Part)
class PartAdmin(admin.ModelAdmin):
    list_display = ('name', 'price', 'specialization')
    list_filter = ('specialization',)
    search_fields = ('name',)

class SpecializationInline(admin.TabularInline):
    model = Master.specializations.through
    extra = 1

@admin.register(Master)
class MasterAdmin(admin.ModelAdmin):
    list_display = ('get_full_name', 'phone_number', 'birth_date', 'created_at')
    list_filter = ('specializations', 'created_at')
    search_fields = ('user__username', 'user__first_name', 'user__last_name', 'phone_number')
    readonly_fields = ('created_at', 'updated_at')
    autocomplete_fields = ('user',)
    inlines = [SpecializationInline]
    exclude = ('specializations',)
    
    def get_full_name(self, obj):
        return f"{obj.user.first_name} {obj.user.last_name}"
    get_full_name.short_description = 'Имя'
    get_full_name.admin_order_field = 'user__first_name'

@admin.register(Client)
class ClientAdmin(admin.ModelAdmin):
    list_display = ('get_full_name', 'phone_number', 'birth_date', 'created_at')
    list_filter = ('created_at',)
    search_fields = ('user__username', 'user__first_name', 'user__last_name', 'phone_number', 'address')
    readonly_fields = ('created_at', 'updated_at')
    autocomplete_fields = ('user',)
    
    def get_full_name(self, obj):
        return f"{obj.user.first_name} {obj.user.last_name}"
    get_full_name.short_description = 'Имя'
    get_full_name.admin_order_field = 'user__first_name'

@admin.register(Order)
class OrderAdmin(admin.ModelAdmin):
    list_display = ('id', 'client', 'master', 'specialization', 'part', 'status', 'price', 'scheduled_date', 'created_at')
    list_filter = ('status', 'scheduled_date', 'created_at', 'part')
    search_fields = ('client__user__username', 'client__user__first_name', 'client__user__last_name', 
                     'master__user__first_name', 'master__user__last_name', 'description')
    readonly_fields = ('created_at', 'updated_at')
    date_hierarchy = 'scheduled_date'
    fieldsets = (
        ('Основная информация', {
            'fields': ('client', 'master', 'specialization', 'part', 'status', 'price')
        }),
        ('Детали заказа', {
            'fields': ('description', 'scheduled_date', 'scheduled_time')
        }),
        ('Служебная информация', {
            'fields': ('created_at', 'updated_at'),
            'classes': ('collapse',)
        }),
    )

@admin.register(Review)
class ReviewAdmin(admin.ModelAdmin):
    list_display = ('client', 'rating', 'short_content', 'created_at')
    list_filter = ('rating', 'created_at')
    search_fields = ('client__user__username', 'client__user__first_name', 'client__user__last_name', 'content')
    readonly_fields = ('created_at', 'updated_at')
    
    def short_content(self, obj):
        return obj.content[:50] + '...' if len(obj.content) > 50 else obj.content
    short_content.short_description = 'Отзыв'

@admin.register(Vacancy)
class VacancyAdmin(admin.ModelAdmin):
    list_display = ('title', 'specialization', 'salary', 'is_active', 'created_at')
    list_filter = ('is_active', 'created_at')
    search_fields = ('title', 'description', 'specialization__name')
    date_hierarchy = 'created_at'
    list_editable = ('is_active',)
    
    fieldsets = (
        (None, {
            'fields': ('title', 'specialization', 'salary', 'is_active')
        }),
        ('Описание', {
            'fields': ('description',)
        }),
    )

class SpecializationForPromocodeInline(admin.TabularInline):
    model = Promocode.specializations.through
    extra = 1
    verbose_name = "Специализация"
    verbose_name_plural = "Специализации"

@admin.register(Promocode)
class PromocodeAdmin(admin.ModelAdmin):
    list_display = ('code', 'discount_display', 'valid_from', 'valid_to', 'is_active', 'is_currently_valid')
    list_filter = ('is_active', 'valid_from', 'valid_to', 'discount_type')
    search_fields = ('code',)
    date_hierarchy = 'valid_to'
    list_editable = ('is_active',)
    exclude = ('specializations',)
    inlines = [SpecializationForPromocodeInline]
    
    fieldsets = (
        (None, {
            'fields': ('code', 'is_active')
        }),
        ('Скидка', {
            'fields': ('discount_type', 'discount_amount')
        }),
        ('Срок действия', {
            'fields': ('valid_from', 'valid_to')
        }),
    )
    
    def discount_display(self, obj):
        if obj.discount_type == 'percentage':
            return f"{obj.discount_amount}%"
        else:
            return f"{obj.discount_amount} руб."
    discount_display.short_description = "Скидка"
    
    def is_currently_valid(self, obj):
        return obj.is_valid()
    is_currently_valid.boolean = True
    is_currently_valid.short_description = "Действующий"

@admin.register(GlossaryTerm)
class GlossaryTermAdmin(admin.ModelAdmin):
    list_display = ('term', 'short_definition', 'added_date', 'updated_at')
    list_filter = ('added_date',)
    search_fields = ('term', 'definition')
    readonly_fields = ('created_at', 'updated_at')
    date_hierarchy = 'added_date'
    
    def short_definition(self, obj):
        return obj.definition[:50] + '...' if len(obj.definition) > 50 else obj.definition
    short_definition.short_description = 'Определение'
