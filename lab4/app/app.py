from flask import Flask, render_template

app = Flask(__name__)

services_list = [
    {"name": "Програмування", "price": 1000},
    {"name": "Дизайн", "price": 500},
    {"name": "Візуалізація даних", "price": 100},
    {"name": "Розробка мобільних додатків", "price": 1200},
    {"name": "Розробка сайтів", "price": 1000},
    {"name": "Розробка інтернет-магазинів", "price": 1000},
]


@app.route('/')
def home():
    return render_template('home.html')


@app.route('/products')
def products():
    return render_template('products.html', services=services_list)


@app.route('/prices')
def prices():
    return render_template('prices.html', services=services_list)


@app.route('/contacts')
def contacts():
    return render_template('contacts.html')


if __name__ == '__main__':
    app.run(port=5000, debug=True)
