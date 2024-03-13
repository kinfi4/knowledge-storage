-- We can make `running` sum using ORDER BY in window functions

SELECT
    category_id,
    sum(category_id) OVER (ORDER BY category_id)
FROM inventory.categories;

-- The same as above, just a lit more complex
SELECT
    order_id,
    line_id,
    lines.sku,
    price,
    sum(price) OVER (PARTITION BY order_id) AS "total for order",
    sum(price) OVER (PARTITION BY order_id ORDER BY line_id)
FROM sales.order_lines lines
    JOIN inventory.products product ON lines.sku = product.sku;