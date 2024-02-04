from django.urls import path, include

from oauth.views import CreateUserProfileView, get_user


urlpatterns = [
    path('', include('rest_auth.urls')),
    path('registration', include('rest_auth.registration.urls')),
    path('fill-profile', CreateUserProfileView.as_view()),
    path('get-user', get_user),
]
