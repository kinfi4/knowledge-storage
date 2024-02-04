from django.db import models
from django.core.validators import FileExtensionValidator


class CustomUser(models.Model):
    display_name = models.CharField(max_length=512)
    email = models.EmailField()
    country = models.CharField(max_length=255)
    join_date = models.DateTimeField(auto_now_add=True)
    avatar = models.ImageField(
        upload_to='avatars/',
        validators=[FileExtensionValidator(allowed_extensions=['jpg'])]
    )

    def is_authenticated(self):
        return True

    def __str__(self):
        return self.email
