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
    (1, 'admin', 'Admin', 'Admin', '$2y$10$V74dRsCZ9dQSroSgVNZIIem5m6dYib.tMdYHmGUiWg1aeLnKAdXoC', 'admin@example.com', '1990-01-01', '2025-06-10 10:00:00', true, 'VAL123', 'FR7612345678901234567890623', false),
    (2, 'jdoe', 'John', 'Doe', '$2y$10$l5agaan9SZLoFlAng9h0z.RUJXo4EwhnuEw82brUu05D5YIdvgVjK', 'jdoe@example.com', '1990-01-01', '2025-06-10 10:00:00', true, 'VAL123', 'FR7612345678901234567890123', false),
    (3, 'asmith', 'Alice', 'Smith', '$2y$10$GOOSlWIekmWS2aOLRLHw.uI2MSCbVJ6XDB6BH5hVIjo8F/n/M7Ud6', 'asmith@example.com', '1985-05-15', '2025-06-10 10:05:00', false, 'VAL456', 'FR7612345678901234567890456', false),
    (4, 'Batman', 'Bruce', 'Wayne', '$2y$10$B2GWx/BZtESlhbgVsLStHOOp2RcW2e.F7j9RgeiLpzsY3wYVpFJ.S', 'bwayne@example.com', '1975-02-19', '2025-06-10 10:10:00', true, 'VAL789', 'FR7612345678901234567890789', false),
    (5, 'Superman', 'Clark', 'Kent', '$2y$10$9PDAaOLaBbhB3KNQ392dTu2YnrwCkf6nju8BMM1SZ3kANEmcpWQTK', 'ckent@example.com', '1978-06-18', '2025-06-10 10:15:00', true, 'VAL101', 'FR7612345678901234567891011', false),
    (6, 'Flash', 'Barry', 'Allen', '$2y$10$9nAmv0j6uNKdzLI7kSGCieuNNwy7ah3mBYPbQSVcXVUyNONvs/SuG', 'ballen@example.com', '1990-03-14', '2025-06-10 10:20:00', true, 'VAL102', 'FR7612345678901234567891022', false),
    (7, 'Joker', 'Joe', 'Kerr', '$2y$10$afkX2y1WVA1z5/lCLv5R9OaIfZe1j1kEpbMi.jp.4y6rOsKJap.AG', 'joker@example.com', '1990-03-14', '2025-06-10 10:20:00', false, 'VAL103', 'FR7612345678901234567891042', true);

INSERT INTO roles_users (user, role) VALUES
    (1, 1), (1, 2),  -- admin as ADMIN & USER
    (2, 3),          -- jdoe as GUEST
    (3, 2),          -- asmith as USER
    (4, 1), (4, 2),  -- Batman as ADMIN & USER
    (5, 2),          -- Superman as USER
    (6, 2),          -- Flash as USER
    (7, 3);          -- Joker as GUEST

INSERT INTO jwt_refresh (id, refresh_token, user_id, issued_at) VALUES
(1, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTIzMDQwMCwiZXhwIjoxNzE5ODM1MjAwfQ.4iBq_sD-F8gZ7kY9xJ6vH5tW3cR2bA1fL0oE9jD4kGc', 1, '2025-06-24 12:00:00');

INSERT INTO adresses (
    id, adress_name, street_number, street_name, zipcode, city, country, region, addendum, floor
) VALUES
    (1, 'Domicile', '12', 'Rue de la Paix', '75002', 'Paris', 'France', 'Île-de-France', NULL, 2),
    (2, 'Bureau', '5', 'Avenue des Champs-Élysées', '75008', 'Paris', 'France', 'Île-de-France', 'Bâtiment B', 4),
    (3, 'Maison de vacances', '8', 'Chemin du Soleil', '06400', 'Cannes', 'France', 'Provence-Alpes-Côte d''Azur', NULL, 1),
    (4, 'JL Headquarters', '1', 'Place of Justice', '12345', 'Metropolis', 'France', 'Paris', NULL, 0),
    (5, '42', '96', 'Boulevard Bessières', '75017', 'Paris', 'France', 'Île-de-France', NULL, 0);

INSERT INTO vehicules (
    id, plate, brand, battery_capacity
) VALUES
    (1, 'AB-123-CD', 'Renault', 50),
    (2, 'XY-456-EF', 'Peugeot', 45),
    (3, 'ZZ-789-GH', 'Tesla', 75),
    (4, 'AA-000-ZZ', 'Volwswagen', 55),
    (5, 'ZZ-999-AA', 'Audi', 60);

INSERT INTO medias (
    id, url, type, media_name, size
) VALUES
    (1, 'https://example.com/image1.jpg', 'image/jpeg', 'Image 1', 204800),
    (2, 'https://example.com/video1.mp4', 'video/mp4', 'Video 1', 10485760),
    (3, 'https://example.com/image2.png', 'image/png', 'Image 2', 102400);

INSERT INTO plug_types (
    id, plug_name
) VALUES
    (1, 'Type 2'),
    (2, 'CHAdeMO'),
    (3, 'CCS Combo'),
    (4, 'Type S');

INSERT INTO spots (
    id, instruction, adress
) VALUES
    (1, 'Parking souterrain niveau -1, badge requis à l''entrée.', 4),
    (2, 'Accès libre, à côté du supermarché.', 1),
    (3, 'Place réservée, demander le code à l''accueil.', 5);

INSERT INTO stations (
    id, station_name, latitude, longitude, price_rate, power_output, manual, state, grounded, busy, wired, spot, owner
) VALUES
    (1, 'Station République', 48.867, 2.363, 0.25, 22.0, 'Voir le manuel à l''accueil.', 'ACTIVE', true, false, true, 3, 1),
    (2, 'Station Gare', 48.844, 2.374, 0.30, 50.0, 'Instructions affichées sur place.', 'INACTIVE', true, true, false, 2, 2),
    (3, 'BatBorne', 48.857194, 2.347063, 0.20, 11.0, NULL, 'ACTIVE', false, false, false, 1, 4),
    (4, 'SuperBorne', 48.857194, 2.347064, 0.20, 11.0, NULL, 'ACTIVE', false, false, false, 1, 5),
    (5, 'SpeedBorne', 48.857194, 2.347065, 0.20, 11.0, NULL, 'ACTIVE', false, false, false, 1, 6);

INSERT INTO reservations (
    id, created_at, validated_at, start_date, end_date, hourly_rate_log, state, payed, date_payed, user, station
) VALUES
    (1, '2025-06-10 10:00:00', NULL, '2025-06-15 09:00:00', '2025-06-15 11:00:00', 12.50, 'ACCEPTED', true, '2025-06-10 10:05:00', 1, 1),
    (2, '2025-06-10 11:00:00', NULL, '2025-06-16 14:00:00', '2025-06-16 16:00:00', 15.00, 'PENDING', false, NULL, 2, 2),
    (3, '2025-06-10 12:00:00', NULL, '2025-06-17 08:00:00', '2025-06-17 10:00:00', 10.00, 'CANCELLED', false, NULL, 1, 2);