CREATE DATABASE IF NOT EXISTS business_case_db;
USE business_case_db;

-- Insert roles
INSERT INTO roles (id, role_name) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_GUEST');

-- Insert admin user
INSERT INTO users (
    id, username, firstname, lastname,
    password, email, birthdate,
    inscription_date, account_valid,
    validation_code, iban, banned
) VALUES
(
    1,
    'admin',
    'Admin',
    'Admin',
    '$2y$10$V74dRsCZ9dQSroSgVNZIIem5m6dYib.tMdYHmGUiWg1aeLnKAdXoC',
    'admin@example.com',
    '1990-01-01',
    '2025-06-10 10:00:00',
    1,                -- true
    'VAL123',
    'FR7612345678901234567890623',
    0                 -- false
);


-- Link admin to roles
INSERT INTO roles_users (`user`, role) VALUES
    (1, 1),
    (1, 2); -- admin as ADMIN & USER


-- Insert refresh token
INSERT INTO refresh_tokens (id, token, user_id, issued_at) VALUES
(
    1,
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTIzMDQwMCwiZXhwIjoxNzE5ODM1MjAwfQ.4iBq_sD-F8gZ7kY9xJ6vH5tW3cR2bA1fL0oE9jD4kGc',
    1,
    '2025-06-24 12:00:00'
);
