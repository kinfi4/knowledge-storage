from django.contrib import admin
from django.urls import path

from test_app.views import BookListView, BookView

urlpatterns = [
    path('admin/', admin.site.urls),
    path('books/', BookListView.as_view()),
    path('books/<int:pk>', BookView.as_view()),
]
