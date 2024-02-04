from rest_framework import serializers

from oauth.models import CustomUser


class GoogleAuthenticationRequestSerializer(serializers.Serializer):
    email = serializers.EmailField()
    token = serializers.CharField()

    def update(self, instance, validated_data):
        pass

    def create(self, validated_data):
        pass


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ('display_name', 'email', 'country', 'join_date', 'avatar')
