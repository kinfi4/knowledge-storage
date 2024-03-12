CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  profile_description TEXT NULL
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    category_id INT NOT NULL,

    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE "order" (
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES "user" (id),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE order_item (
    id         SERIAL PRIMARY KEY,
    order_id   INT REFERENCES "order" (id),
    product_id INT REFERENCES product (id),
    quantity   INT NOT NULL
);

CREATE TABLE review (
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES "user" (id),
    product_id INT REFERENCES product (id),
    rating     INT NOT NULL,
    comment    TEXT NULL,
    review_date TIMESTAMP DEFAULT NOW(),

    CONSTRAINT rating_range CHECK ( rating >= 1 AND rating <= 5 )
);

CREATE TABLE user_login (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES "user" (id),
    login_date TIMESTAMP DEFAULT NOW(),
    logout_date TIMESTAMP NULL,
    ip_address VARCHAR(255) NOT NULL
);