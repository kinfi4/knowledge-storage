from django.shortcuts import render
from django.http import HttpRequest
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework import status

from oauth.serializers import GoogleAuthenticationRequestSerializer
from oauth.services.google import varify_google_token


def google_login(request: HttpRequest):
    return render(request, 'oauth/google-login.html')


class GoogleAuthenticationView(APIView):
    def post(self, request: Request):
        print(request.data)

        serialized_request = GoogleAuthenticationRequestSerializer(data=request.data)

        if serialized_request.is_valid():
            data = varify_google_token(serialized_request.data['email'], serialized_request.data['token'])

            return Response(data=data['token'])

        return Response(status=status.HTTP_400_BAD_REQUEST, data=serialized_request.errors)
