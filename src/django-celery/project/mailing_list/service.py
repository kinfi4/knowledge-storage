from django.conf import settings
from django.core.mail import send_mail

from mailing_list.models import Contact


def send_email(contact_info: dict):
    return send_mail(
        subject=f'Hi, {contact_info.get("username")}. You were subscribe on mailing list',
        message='Now every day, we are going to send you lots of spam',
        from_email=settings.EMAIL_HOST_USER,
        recipient_list=[contact_info.get('email')],
        fail_silently=True
    )
