BEGIN;

SELECT * FROM "user" WHERE username = 'johndoe' FOR SHARE ;
-- LOCK TABLE "user" IN ROW SHARE MODE;

ROLLBACK ;