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
-- NUEVA TABLA: asignaciones
-- ==========================================

CREATE TABLE asignaciones (
                              id INT AUTO_INCREMENT PRIMARY KEY,

                              cliente_id INT NOT NULL,

                              rutina_id INT NOT NULL,

                              fecha_inicio DATE NOT NULL,

                              fecha_fin DATE NOT NULL,

                              estado ENUM('ACTIVA','FINALIZADA') DEFAULT 'ACTIVA',

                              FOREIGN KEY (cliente_id) REFERENCES clientes(id),

                              FOREIGN KEY (rutina_id) REFERENCES rutinas(id)
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

-- ==========================================
-- Datos de prueba para asignaciones
-- ==========================================

INSERT INTO asignaciones
(cliente_id, rutina_id, fecha_inicio, fecha_fin, estado)
VALUES
    (1, 2, '2026-07-19', '2026-09-19', 'ACTIVA'),
    (2, 1, '2026-07-10', '2026-08-10', 'ACTIVA'),
    (3, 3, '2026-06-01', '2026-07-15', 'FINALIZADA');