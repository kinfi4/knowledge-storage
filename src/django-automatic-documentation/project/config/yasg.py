from rest_framework.permissions import AllowAny
from drf_yasg.views import get_schema_view
from drf_yasg.openapi import Info, Contact
from django.urls import path


schema_view = get_schema_view(
    Info(
        title='Test App',
        description='Some text here describing the app',
        default_version='v1',
        contact=Contact('github/kinfi4')
    ),
    public=True,
    permission_classes=(AllowAny,)
)

urlpatterns = [
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='swagger-with-ui'),
    path('redoc/', schema_view.with_ui('redoc', cache_timeout=0), name='redoc-with-ui'),
]
