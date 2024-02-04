from django.contrib import admin
from django.urls import path

import mailing_list.views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', mailing_list.views.ContactView.as_view()),
]
