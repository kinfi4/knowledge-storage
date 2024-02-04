from django.urls import path

from oauth.endpoints.auth_views import google_login, GoogleAuthenticationView
from oauth.endpoints.user_views import UserView


urlpatterns = [
    path('', google_login),
    path('google/', GoogleAuthenticationView.as_view()),
    path('me/', UserView.as_view({'get': 'retrieve', 'put': 'update'})),
]
