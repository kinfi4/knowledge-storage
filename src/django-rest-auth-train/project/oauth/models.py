from django.contrib.auth.models import User
from django.db import models


class MyUserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='user_profile')
    bio = models.TextField()

    def __str__(self):
        return self.user.username
