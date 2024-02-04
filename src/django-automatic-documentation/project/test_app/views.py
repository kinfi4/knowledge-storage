import random
import lorem
from string import ascii_lowercase

from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework import status


class SimpleListView(APIView):
    """
        This is a simple view used to see how swagger documentation works
    """

    def get(self, request: Request):
        """
            GET method, returns a random number and a random string
        """

        simple_number = random.randint(10, 1000)
        simple_string = lorem.sentence()

        response_data = {'number': simple_number, 'text': simple_string}

        return Response(data=response_data)

    def post(self, request: Request):
        """
            POST method, gets a number and returns it doubled, in case if number is greater or equal zero and
            400 BAD REQUEST otherwise
        """

        number = request.data['number']

        if not isinstance(number, int) or number < 0:
            return Response(status=status.HTTP_400_BAD_REQUEST, data={'error': 'number must be >= 0'})

        response_data = {'doubled_number': number * 2}
        return Response(data=response_data)

    def put(self, request: Request):
        """
            PUT method, gets a number of points and return tripled number, in case if points number is less than 10
            and 400 BAD REQUEST otherwise
        """

        number = request.data['points']

        if not isinstance(number, int) or number < 10:
            return Response(status=status.HTTP_400_BAD_REQUEST, data={'error': 'number must be >= 10'})

        response_data = {'tripled_points': number * 3}
        return Response(data=response_data)


class SimpleView(APIView):
    """
        This is another simple View which has only one GET method with params
    """

    def get(self, request: Request, length: int):
        """
            GET method, which gets pk as a parameter and returns random text with this length
        """

        random_string = ''.join([random.choice(ascii_lowercase) for _ in range(length)])

        return Response(data={'random_string': random_string})
