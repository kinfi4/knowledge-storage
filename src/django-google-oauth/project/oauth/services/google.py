from google.oauth2 import id_token
from google.auth.transport import requests
from rest_framework.exceptions import AuthenticationFailed

from oauth.models import CustomUser
from oauth.services.auth_backend import create_jwt_token


def varify_google_token(email, token):
    try:
        id_token.verify_oauth2_token(token, request=requests.Request())
    except ValueError:
        raise AuthenticationFailed(code=403, detail='Invalid google auth token')

    user, _ = CustomUser.objects.get_or_create(email=email)

    return {
        'user_id': user.id,
        'token': create_jwt_token(user.id)
    }
