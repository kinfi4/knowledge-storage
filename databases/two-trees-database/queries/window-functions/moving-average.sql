SELECT
    order_date,
    avg(products.price) OVER (ROWS BETWEEN 3 PRECEDING AND 3 FOLLOWING) AS "moving avg"
FROM sales.orders
    JOIN sales.order_lines ON orders.order_id = order_lines.order_id
    JOIN inventory.products ON products.sku = order_lines.sku
ORDER BY order_date;


-- Я хочу получить customer + когда он сделал первый свой запрос + когда он сделал последний запрос
SELECT
    customer_id,
    min(order_date),
    max(order_date)
FROM sales.orders
    GROUP BY customer_id;
-- Тоже самое можно сделать через Window Functions (но вообще это хуже работает)
SELECT
    DISTINCT customer_id,
    first_value(order_date)
        OVER (PARTITION BY customer_id ORDER BY order_date ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),
    last_value(order_date)
        OVER (PARTITION BY customer_id ORDER BY order_date ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
FROM sales.orders;
