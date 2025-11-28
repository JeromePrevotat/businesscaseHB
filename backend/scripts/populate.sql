CREATE DATABASE IF NOT EXISTS db;
USE db;

-- Disable foreign key checks to allow truncation
-- and insertion without constraint violations
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE reservations;
TRUNCATE TABLE stations;
TRUNCATE TABLE spots;
TRUNCATE TABLE plug_types;
TRUNCATE TABLE medias;
TRUNCATE TABLE vehicules;
TRUNCATE TABLE adresses;
TRUNCATE TABLE users;
TRUNCATE TABLE roles_users;
TRUNCATE TABLE roles;
TRUNCATE TABLE jwt_refresh;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO roles (id, role_name) VALUES
    (1, 'ADMIN'),
    (2, 'USER'), 
    (3, 'GUEST');

INSERT INTO users (id, username, firstname,
                    lastname, password, email,
                    birthdate, inscription_date, account_valid,
                    validation_code, iban,
                    banned)
VALUES
    (1, 'admin', 'Admin', 'Admin', '$2y$10$V74dRsCZ9dQSroSgVNZIIem5m6dYib.tMdYHmGUiWg1aeLnKAdXoC', 'admin@example.com', '1990-01-01', '2025-06-10 10:00:00', true, 'VAL123', 'FR7612345678901234567890623', false);

INSERT INTO roles_users (user, role) VALUES
    (1, 1), (1, 2),  -- admin as ADMIN & USER

INSERT INTO jwt_refresh (id, refresh_token, user_id, issued_at) VALUES
(1, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTIzMDQwMCwiZXhwIjoxNzE5ODM1MjAwfQ.4iBq_sD-F8gZ7kY9xJ6vH5tW3cR2bA1fL0oE9jD4kGc', 1, '2025-06-24 12:00:00');
