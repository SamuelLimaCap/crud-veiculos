INSERT INTO permissions (permission) values
    SELECT "CLIENT" WHERE NOT EXISTS (SELECT 1 FROM user_permissions WHERE permission = "CLIENT");


INSERT INTO permissions (permission) values
SELECT "BUYER" WHERE NOT EXISTS (SELECT 1 FROM user_permissions WHERE permission = "BUYER");


INSERT INTO permissions (permission) values
    SELECT "SELLER" WHERE NOT EXISTS (SELECT 1 FROM user_permissions WHERE permission = "SELLER");

