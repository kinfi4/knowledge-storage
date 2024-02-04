from django.urls import path

from test_app.views import SimpleListView, SimpleView

urlpatterns = [
    path('endpoint/', SimpleListView.as_view()),
    path('endpoint/<int:length>', SimpleView.as_view()),
]
