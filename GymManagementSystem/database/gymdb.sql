-- ==========================================
-- Gym Management System
-- Base de datos inicial
-- ==========================================

DROP DATABASE IF EXISTS gymdb;
CREATE DATABASE gymdb;
USE gymdb;

-- ==========================================
-- Tabla de usuarios
-- ==========================================

CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          usuario VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol ENUM('cliente','entrenador') NOT NULL
);

-- ==========================================
-- Tabla de clientes
-- ==========================================

CREATE TABLE clientes (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          edad INT NOT NULL,
                          peso DECIMAL(5,2) NOT NULL
);

-- ==========================================
-- Tabla de rutinas
-- ==========================================

CREATE TABLE rutinas (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         objetivo VARCHAR(150) NOT NULL,
                         duracion INT NOT NULL
);

-- ==========================================
-- Datos iniciales
-- ==========================================

INSERT INTO usuarios (usuario, password, rol)
VALUES
    ('cliente', '1234', 'cliente'),
    ('entrenador', '1234', 'entrenador');

INSERT INTO clientes (nombre, edad, peso)
VALUES
    ('Joel Arevalo', 20, 75.50),
    ('Carlos Perez', 24, 82.00),
    ('Maria Lopez', 21, 60.30),
    ('Luis Gomez', 28, 90.10);

INSERT INTO rutinas (nombre, objetivo, duracion)
VALUES
    ('Cardio Basico', 'Perder peso', 30),
    ('Hipertrofia', 'Ganar masa muscular', 60),
    ('Funcional', 'Mejorar resistencia', 45);