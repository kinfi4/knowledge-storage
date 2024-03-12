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


-- getting orders information along with product name and total sold quantity
WITH orders_with_total_sold_items__cte AS (
    SELECT o.id, sum(o_i.quantity) AS total_items_sold FROM "order" o
    JOIN order_item o_i ON o.id = o_i.order_id
    GROUP BY o.id
)
SELECT o.id, u.username, cte.total_items_sold FROM "order" o
    JOIN orders_with_total_sold_items__cte cte ON o.id = cte.id
    JOIN "user" u ON u.id = o.user_id
ORDER BY cte.total_items_sold DESC
LIMIT 5;


















WITH order_with_total_sold__cte AS (
    SELECT order_id, sum(order_item.quantity) AS total_items_sold FROM "order"
    JOIN order_item ON "order".id = order_item.order_id
    GROUP BY order_id
)
SELECT order_id, username, cte.total_items_sold FROM "order"
    JOIN order_with_total_sold__cte cte ON "order".id = cte.order_id
    JOIN "user" ON "order".user_id = "user".id
ORDER BY cte.total_items_sold DESC
LIMIT 5;


SELECT order_id, username, sum(order_item.quantity) as total_items_sold FROM "order"
    JOIN order_item ON "order".id = order_item.order_id
    JOIN "user" ON "order".user_id = "user".id
GROUP BY order_id, username
ORDER BY total_items_sold DESC
LIMIT 5;


SELECT
    o.id,
    username,
    (
        SELECT
            CASE WHEN sum(order_item.quantity) IS NULL THEN 0 ELSE sum(order_item.quantity) END
        FROM order_item
            WHERE order_item.order_id = o.id
    ) AS total_items_sold
FROM "order" o
JOIN "user" ON o.user_id = "user".id
ORDER BY total_items_sold DESC;














