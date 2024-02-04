from django.contrib import admin

from test_app.models import Author, Book


admin.site.register(Author)
admin.site.register(Book)
