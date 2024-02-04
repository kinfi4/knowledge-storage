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
