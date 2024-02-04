from django.urls import path
from drf_yasg.views import get_schema_view
from drf_yasg.openapi import Info, Contact
from rest_framework.permissions import AllowAny


schema = get_schema_view(
    info=Info(
        title='Google auth test project',
        contact=Contact('github/kinfi4'),
        description='This is a test project, in order to train myself building custom google auth',
        default_version='v1'
    ),
    public=True,
    permission_classes=(AllowAny,)
)

urlpatterns = [
    path('swagger/', schema.with_ui('swagger', cache_timeout=0), name='swagger_with_ui'),
]
