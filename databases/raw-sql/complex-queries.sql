-- UPSERT
INSERT INTO "user" (username, email, profile_description) VALUES
('johndoe', 'johndoe@example.com', 'Enthusiastic tech blogger and coder!!!')
ON CONFLICT (username)
DO UPDATE
SET username = EXCLUDED.username, email = EXCLUDED.email, profile_description = EXCLUDED.profile_description;


-- Profile the query
EXPLAIN ANALYZE
SELECT
    name AS product_name,
    AVG(rating) AS average_rating,
    COUNT(rating) AS number_of_ratings
FROM "user"
    JOIN public.review r on "user".id = r.user_id
    JOIN public.product p on r.product_id = p.id
GROUP BY product_name;




-----------------------------------------
-- Making advanced filtering for searching the most expensive something


-- Get most expensive product for each category
SELECT
    c.name,
    p.name,
    p.price
FROM category c
JOIN product p ON c.id = p.category_id
WHERE p.price = (SELECT max(price) FROM product WHERE category_id = c.id);


-- Get second most expensive product for each category
SELECT
    c.name,
    p.name,
    p.price
FROM category c
JOIN product p ON c.id = p.category_id
WHERE p.price = (
    SELECT max(price) FROM product
    WHERE category_id = c.id AND price != (SELECT max(price) FROM product WHERE category_id = c.id)
);


-- there goes the same but using Window functions
SELECT subquery.categoryName, subquery.productName, subquery.productPrice FROM (
    SELECT
    c.name AS categoryName,
    p.name AS productName,
    p.price AS productPrice,
    row_number() over (PARTITION BY category_id ORDER BY p.price DESC) AS idx
FROM category c
JOIN product p ON c.id = p.category_id ) subquery
WHERE subquery.idx = 2;


-----------------------------------------
-- Select for each category the product was sold most times along with this product name
SELECT
    c.name AS categoryName,
    p.name AS productName,
    sum(o.quantity) AS totalProductsSold
FROM category c
    JOIN product p ON p.category_id = c.id
    JOIN order_item o ON o.product_id = p.id
WHERE p.name = (
    SELECT
        product.name
    FROM product JOIN order_item ON order_item.product_id = product.id
    WHERE product.category_id = c.id
    GROUP BY product.name
    ORDER BY sum(order_item.quantity) DESC LIMIT 1
)
GROUP BY c.name, p.name;
