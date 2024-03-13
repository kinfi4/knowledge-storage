SELECT
    product_name,
    price,
    rank() OVER (ORDER BY price) AS "rank price",  -- 1,2,3,3,5,6,6,6,9
    dense_rank() OVER (ORDER BY price) AS "dense rank"  -- 1,2,3,3,4,5,5,5,6
FROM inventory.products;


SELECT
    category_description,
    product_name,
    price,
    rank() OVER (PARTITION BY category_description ORDER BY price)
FROM inventory.products
    JOIN inventory.categories ON categories.category_id = products.category_id
ORDER BY category_description, product_name;


SELECT
    product_name,
    price,
    percent_rank() OVER (ORDER BY price),
    CASE
        WHEN percent_rank() OVER (ORDER BY price) < 0.25 THEN 'First quartile'
        WHEN percent_rank() OVER (ORDER BY price) < 0.5 THEN 'Second quartile'
        WHEN percent_rank() OVER (ORDER BY price) < 0.75 THEN 'Third quartile'
        ELSE 'Fourth quartile'
    END AS "price quartile"
FROM inventory.products
ORDER BY price;


SELECT
    customer_id,
    order_date,
    LAG(order_date, 1) OVER (PARTITION BY customer_id ORDER BY order_date) AS "previous order date",
    LEAD(order_date, 1) OVER (PARTITION BY customer_id ORDER BY order_date) AS "next order date",
    LEAD(order_date, 1) OVER (PARTITION BY customer_id ORDER BY order_date) - orders.order_date AS "days between orders"
FROM sales.orders
ORDER BY customer_id, order_date;


-- get every 5 orders
WITH numbered_orders AS (
    SELECT
        row_number() over () AS order_idx,
        order_date,
        customer_id
    FROM sales.orders
)
SELECT * FROM numbered_orders
WHERE numbered_orders.order_idx % 5 = 0;
