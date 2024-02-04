from django.core.mail import send_mail
from django.conf import settings

from config.celery import app
from mailing_list.models import Contact
from mailing_list.service import send_email as my_send_email


@app.task
def send_email_task(contact_info: dict):
    my_send_email(contact_info)


@app.task
def send_spam_email():
    for contact in Contact.objects.all():
        send_mail(
            'Test message every 5 minutes',
            'I am going to do that every 5 minutes',
            settings.EMAIL_HOST_USER,
            [contact.email],
            fail_silently=True
        )
