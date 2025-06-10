CREATE DATABASE IF NOT EXISTS db;
USE db;


INSERT INTO users (username, firstname, lastname, password, email, birthdate, inscription_date, account_valid, validation_code, role, iban, banned)
VALUES
    ('johndoe', 'John', 'Doe', 'password123', 'johndoe@example.com', '1990-01-01', NOW(), true, 'abc123', 'ADMIN', 'FR7612345678901234567890123', false),
    ('janedoe', 'Jane', 'Doe', 'password456', 'janedoe@example.com', '1992-02-02', NOW(), true, 'def456', 'ADMIN', 'FR7612345678901234567890124', false),
    ('alice', 'Alice', 'Smith', 'password789', 'alice@example.com', '1995-03-03', NOW(), true, 'ghi789', 'REGISTERED', 'FR7612345678901234567890125', false),
    ('bob', 'Bob', 'Johnson', 'password101', 'bob@example.com', '1988-04-04', NOW(), true, 'jkl012', 'REGISTERED', 'FR7612345678901234567890126', false);