-- ALTER TABLE users ADD CONSTRAINT unique_login UNIQUE (login);
ALTER TABLE users ADD COLUMN email varchar(100);