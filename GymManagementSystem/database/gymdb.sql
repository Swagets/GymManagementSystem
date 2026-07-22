-- ==========================================
-- Gym Management System
-- Base de datos con usuarios relacionados
-- ==========================================

DROP DATABASE IF EXISTS gymdb;
CREATE DATABASE gymdb;
USE gymdb;


-- ==========================================
-- Tabla de usuarios
-- Maneja el acceso al sistema
-- ==========================================

CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          usuario VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol ENUM('cliente','entrenador') NOT NULL
);


-- ==========================================
-- Tabla de clientes
-- Información personal del cliente
-- Relacionada con usuarios
-- ==========================================

CREATE TABLE clientes (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          edad INT NOT NULL,
                          peso DECIMAL(5,2) NOT NULL,

                          usuario_id INT NOT NULL,

                          FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
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
-- Tabla de asignaciones
-- Relaciona clientes con rutinas
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
-- USUARIO INICIAL DEL SISTEMA
-- Será el entrenador/admin
-- ==========================================

INSERT INTO usuarios(usuario,password,rol)
VALUES
    ('admin','1234','entrenador');


-- ==========================================
-- CLIENTE DE PRUEBA
-- Primero se crea su usuario
-- ==========================================

INSERT INTO usuarios(usuario,password,rol)
VALUES
    ('joel','1234','cliente');


-- ==========================================
-- Luego se crea su información personal
-- Relacionada con usuario_id = 2
-- ==========================================

INSERT INTO clientes(nombre,edad,peso,usuario_id)
VALUES
    ('Joel Arevalo',20,75.50,2);


-- ==========================================
-- Otros clientes sin cuenta todavía
-- Estos luego los creará el entrenador
-- ==========================================

INSERT INTO usuarios(usuario,password,rol)
VALUES
    ('carlos','1234','cliente'),
    ('maria','1234','cliente');


INSERT INTO clientes(nombre,edad,peso,usuario_id)
VALUES
    ('Carlos Perez',24,82.00,3),
    ('Maria Lopez',21,60.30,4);



-- ==========================================
-- Rutinas iniciales
-- ==========================================

INSERT INTO rutinas(nombre,objetivo,duracion)
VALUES
    ('Cardio Basico','Perder peso',30),
    ('Hipertrofia','Ganar masa muscular',60),
    ('Funcional','Mejorar resistencia',45);



-- ==========================================
-- Asignaciones de prueba
-- ==========================================

INSERT INTO asignaciones
(cliente_id,rutina_id,fecha_inicio,fecha_fin,estado)
VALUES
    (1,2,'2026-07-19','2026-09-19','ACTIVA'),

    (2,1,'2026-07-10','2026-08-10','ACTIVA'),

    (3,3,'2026-06-01','2026-07-15','FINALIZADA');