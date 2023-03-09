FROM php:7.4-apache
RUN docker-php-ext-install mysqli

COPY ./app /var/www/html/
COPY ./app/books.php /var/www/html/index.php
