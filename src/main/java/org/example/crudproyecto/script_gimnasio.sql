-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gimnasio_db;
USE gimnasio_db;

-- Limpiar la tabla si existía
DROP TABLE IF EXISTS clientes;

-- Crear la tabla clientes
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20)
);

-- Insertar datos de prueba iniciales
INSERT INTO clientes (nombre, email, telefono) VALUES
('Juan Pérez', 'juan.perez@example.com', '0991234567'),
('Maria López', 'maria.lopez@example.com', '0987654321'),
('Carlos Mendoza', 'carlos.mendoza@example.com', '0998887776');