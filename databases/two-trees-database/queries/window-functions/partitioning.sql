
-- get products with avg price for product category
SELECT
    c.category_description AS category_description,
    p.product_name,
    p.price,
    round(avg(p.price) OVER (PARTITION BY c.category_description), 3) AS avg_category_price,
    p.price - avg(p.price) OVER (PARTITION BY c.category_description) AS product_price_category_price_diff,
    CASE WHEN p.price < avg(p.price) OVER (PARTITION BY c.category_description) THEN 'less' ELSE 'more' END
FROM inventory.products p
    JOIN inventory.categories c ON p.category_id = c.category_id
ORDER BY c.category_description;


-- We can define partition as param
SELECT
    product_name,
    size,
    avg(price) OVER (window_name) AS avg_price_for_size,
    min(price) OVER (window_name) AS min_price_for_size,
    max(price) OVER (window_name) AS max_price_for_size
FROM inventory.products
WINDOW window_name AS (PARTITION BY size)
ORDER BY avg_price_for_size DESC;