CREATE DATABASE IF NOT EXISTS business_case_db;
USE business_case_db;

-- Insert roles
INSERT INTO roles (id, role_name) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_GUEST');


INSERT INTO users (
    id, username, firstname, lastname,
    password, email, birthdate,
    inscription_date, account_valid,
    validation_code, iban, banned
) VALUES
-- Insert admin user
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
),
-- Insert regular user
( 
    2,
    'user',
    'User',
    'User',
    '$2a$10$xqprgdM4rnnbf7BQQICYNujhSguArB2raQFgPqTQsQwbpSWp7fBXO',
    'user@user.com',
    '1992-07-26',
    '2025-12-27 00:58:59',
    1,                -- true
    '687269',
    NULL,
    0                 -- false
);


-- Link admin to roles
INSERT INTO roles_users (`user`, role) VALUES
    (1, 1),
    (1, 2), -- admin as ADMIN & USER
    (2, 2); -- user as USER

-- Insert refresh token
INSERT INTO refresh_tokens (id, token, user_id, issued_at) VALUES
(
    1,
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTIzMDQwMCwiZXhwIjoxNzE5ODM1MjAwfQ.4iBq_sD-F8gZ7kY9xJ6vH5tW3cR2bA1fL0oE9jD4kGc',
    1,
    '2025-06-24 12:00:00'
);


INSERT INTO stations (
    id, station_name, latitude, longitude, owner,
    state, busy, grounded, wired, power_output, price_rate,
    spot, manual
) VALUES
(
    1,
    'Station Test',
    45.7582, 3.1267,
    1, -- Owner (admin)
    'ACTIVE',
    0, -- busy
    1, -- grounded
    1, -- wired
    22.0,
    0.30,
    NULL,
    'Plug & Charge'
),
(
    2,
    'Station Test 2',
    45.7611, 3.1205,
    1, -- Owner (admin)
    'ACTIVE',
    0, -- busy
    1, -- grounded
    1, -- wired
    22.0,
    1.10,
    NULL,
    'Plug & Charge'
),
(
    3,
    'Station Test',
    48.8907, 2.3198,
    1, -- Owner (admin)
    'ACTIVE',
    0, -- busy
    1, -- grounded
    1, -- wired
    22.0,
    0.30,
    NULL,
    'Plug & Charge'
),
(
    4,
    'Station Test 4',
    48.8851, 2.3293,
    1, -- Owner (admin)
    'ACTIVE',
    0, -- busy
    1, -- grounded
    1, -- wired
    22.0,
    1.10,
    NULL,
    'Plug & Charge'
)
;