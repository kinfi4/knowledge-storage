from django import forms


class ContactForm(forms.Form):
    username = forms.CharField(max_length=255)
    email = forms.EmailField()
