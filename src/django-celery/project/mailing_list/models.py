from django.db import models


class Contact(models.Model):
    username = models.CharField(max_length=255)
    email = models.EmailField()

    def __str__(self):
        return f'{self.username} - {self.email}'
