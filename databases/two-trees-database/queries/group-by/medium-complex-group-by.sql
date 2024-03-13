SELECT
    CASE WHEN size < 10 THEN 'tiny' WHEN size < 30 THEN 'small' ELSE 'large' END AS "size category",
    count(*)
FROM inventory.products
GROUP BY "size category";
-- in the previous version of postgres we would have to do:
-- GROUP BY WHEN size < 10 THEN 'tiny' WHEN size < 30 THEN 'small' ELSE 'large' END


SELECT
    state,
    bool_and(newsletter) AS "analog of 'all' function",
    bool_or(newsletter) AS "analog of 'any' function"
FROM sales.customers
GROUP BY state;
