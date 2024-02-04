from rest_framework.viewsets import ModelViewSet
from rest_framework.parsers import MultiPartParser
from rest_framework.permissions import IsAuthenticated

from oauth.serializers import UserSerializer


class UserView(ModelViewSet):
    serializer_class = UserSerializer
    parser_classes = (MultiPartParser,)
    permission_classes = (IsAuthenticated,)

    def get_queryset(self):
        return self.request.user

    def get_object(self):
        return self.request.user
