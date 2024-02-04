from rest_framework.decorators import api_view, authentication_classes
from rest_framework.request import Request
from rest_framework.authentication import TokenAuthentication
from rest_framework.response import Response
from rest_framework.views import APIView

from oauth.models import User, MyUserProfile


class CreateUserProfileView(APIView):
    authentication_classes = (TokenAuthentication, )  # or set DEFAULT_AUTHENTICATION_CLASSES in settings.py

    def post(self, request: Request):
        user_bio_text = request.data['bio']
        print(request.user)
        # create user bio object
        user_bio, _ = MyUserProfile.objects.get_or_create(bio=user_bio_text, user=request.user)

        return Response(data={'user_bio': user_bio.bio})


@api_view()
@authentication_classes((TokenAuthentication, ))
def get_user(request: Request):
    user = User.objects.get(pk=request.user.id)

    return Response(data={'user_name': user.username, 'bio': user.user_profile.bio})
