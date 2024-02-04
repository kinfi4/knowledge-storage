from datetime import timedelta, datetime

import jwt
from django.conf import settings

from django.core.exceptions import ObjectDoesNotExist
from rest_framework.authentication import BaseAuthentication, get_authorization_header
from rest_framework.exceptions import AuthenticationFailed
from rest_framework.request import Request

from oauth.models import CustomUser


class CustomAuthentication(BaseAuthentication):
    def authenticate(self, request: Request):
        if 'docs/' in request._request.path:  # I added this line, in order to user swagger docs without authentication
            return CustomUser.objects.all()[0], None

        token_header = get_authorization_header(request)  # is has structure: Token la98dysfpaoi289347p9a;iofha;lsdkfj9
        token_header = token_header.split()

        if not token_header or token_header[0].lower() != b'token':
            raise AuthenticationFailed(detail='Authentication header does not specified or has invalid form')
        if len(token_header) >= 3:
            raise AuthenticationFailed(detail='Authentication header has spaces')
        if len(token_header) == 1:
            raise AuthenticationFailed(detail='Authentication header does not contains token')

        token = token_header[1].decode('utf-8')
        return self.get_authenticated_user(token)

    def get_authenticated_user(self, jwt_token):
        try:
            decoded_token = jwt.decode(jwt_token, key=settings.SECRET_KEY, algorithms='HS256')
        except jwt.DecodeError:
            raise AuthenticationFailed(detail='Could not decode token, it may contains invalid characters')

        if datetime.utcnow() > datetime.fromtimestamp(decoded_token['exp']):
            raise AuthenticationFailed(detail='Token has expired')

        try:
            user = CustomUser.objects.get(pk=decoded_token['user_id'])
        except ObjectDoesNotExist:
            raise AuthenticationFailed(detail='User for specified token was not found')

        return user, None


def create_jwt_token(user_id):
    token_duration = timedelta(minutes=settings.TOKEN_LIFE_MINUTES)
    token_expiration_time = datetime.utcnow() + token_duration

    to_encode = {'user_id': user_id, 'exp': token_expiration_time, 'sub': 'access'}

    encoded_token = jwt.encode(to_encode, algorithm='HS256', key=settings.SECRET_KEY)

    return encoded_token
