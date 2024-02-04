from django.core.cache import cache
from django.core.exceptions import ObjectDoesNotExist
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response

from test_app.models import Book


class BookListView(APIView):
    def get(self, request: Request):
        books = cache.get('books_list')

        if not books:
            books = Book.objects.all().select_related('author')
            print('Searching for books in DB')
            cache.set('book_list', books)

        return Response(data={'books': [book.to_dict() for book in books]})


class BookView(APIView):
    def get(self, request: Request, pk: int):
        book = cache.get(f'book_id_{pk}')

        if not book:
            try:
                book = Book.objects.get(pk=pk)
                print('Searching for book in DB')
                cache.set(f'book_id_{pk}', book)
            except ObjectDoesNotExist:
                return Response(status=404, data={'error': f'Book with pk={pk} not found'})

        return Response(data=book.to_dict())
