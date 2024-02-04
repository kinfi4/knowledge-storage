from django.shortcuts import render
from django.http import HttpRequest
from django.views import View
from django.forms.models import model_to_dict

from mailing_list.forms import ContactForm
from mailing_list.models import Contact
from mailing_list.tasks import send_email_task


class ContactView(View):
    def get(self, request: HttpRequest):
        form = ContactForm()

        return render(request, 'contact-form.html', context={'form': form})

    def post(self, request: HttpRequest):
        form = ContactForm(request.POST)

        if form.is_valid():
            contact, _ = Contact.objects.get_or_create(email=form.data.get('email'), username=form.data.get('username'))
            send_email_task.delay(model_to_dict(contact))

        return render(request, 'contact-form.html', context={'form': ContactForm()})
