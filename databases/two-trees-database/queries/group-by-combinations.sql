
----- GET DATA FOR EACH CATEGORY AND PRODUCT AND FOR EACH CATEGORY
(
    SELECT
        category.category_description,
        product.product_name,
        AVG(product.price) AS average_price
    FROM inventory.products product
    JOIN inventory.categories category ON product.category_id = category.category_id
    GROUP BY category.category_description, product.product_name
)
UNION
(
    SELECT
        category.category_description,
        NULL AS product_name,
        AVG(product.price) AS average_price
    FROM inventory.products product
    JOIN inventory.categories category ON product.category_id = category.category_id
    GROUP BY category.category_description
)
ORDER BY category_description, product_name DESC;


-- DO THE SAME BUT WITH ROLLUP
SELECT
    category.category_description,
    product.product_name,
    AVG(product.price) AS average_price
FROM inventory.products product
    JOIN inventory.categories category ON product.category_id = category.category_id
GROUP BY ROLLUP (category.category_description, product.product_name)
ORDER BY category_description, product_name DESC;



-- WE CAN ALSO USE `CUBE` TO GET ALL POSSIBLE COMBINATIONS OF GROUPS

-- THIS WILL GIVE US THE TOTALS FOR EACH CATEGORY, PRODUCT, AND SIZE
-- AS WELL AS THE TOTALS FOR EACH CATEGORY AND PRODUCT
-- AND THE TOTALS FOR EACH CATEGORY
-- AND THE TOTALS FOR EACH PRODUCT
-- AND THE TOTALS FOR EACH SIZE
-- AND THE TOTALS FOR EACH SIZE AND PRODUCT
-- AND THE TOTALS FOR EACH SIZE AND CATEGORY
SELECT
    category.category_description AS category_name,
    product.size,
    product_name,
    count(*) AS product_count
FROM inventory.products product
    JOIN inventory.categories category ON product.category_id = category.category_id
GROUP BY CUBE (category.category_description, product_name, product.size)
ORDER BY category_description, product_name DESC;
