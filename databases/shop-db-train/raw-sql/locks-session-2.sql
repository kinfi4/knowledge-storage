BEGIN;

UPDATE "user" SET profile_description = 'some new description' WHERE username = 'johndoe' ;

SELECT * FROM "user" WHERE username = 'johndoe' ;
SELECT * FROM "user";

ROLLBACK ;