SELECT
    category_description AS category_name,
    COUNT(*) AS total_products_in_category,
    COUNT(*) FILTER ( WHERE product.size < 10 ) AS small_products_in_category,
    COUNT(*) FILTER ( WHERE product.size >= 10 ) AS large_products_in_category
 FROM inventory.products product
    JOIN inventory.categories category ON product.category_id = category.category_id
GROUP BY category_description;