from django.db import models


class Author(models.Model):
    name = models.CharField(max_length=255)

    def __str__(self):
        return self.name


class Book(models.Model):
    title = models.CharField(max_length=255)
    author = models.ForeignKey(Author, on_delete=models.CASCADE, related_name='books')

    def to_dict(self):
        return {
            "title": self.title,
            "author": self.author.name
        }

    def __str__(self):
        return self.title
