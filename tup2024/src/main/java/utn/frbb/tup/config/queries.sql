CREATE DATABASE BANCO;
USE BANCO;

CREATE TABLE CLIENTES (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(55) NOT NULL,
    apellido VARCHAR(55) NOT NULL,
    dni BIGINT UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tipo ENUM ('F', 'J') NOT NULL,
    banco VARCHAR(55) NOT NULL,
    alta DATETIME NOT NULL
);

CREATE TABLE CUENTAS (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nro_asociado LONG NOT NULL,
    tipo ENUM('A', 'C'),
    creacion DATETIME NOT NULL,
    saldo DOUBLE NOT NULL,
    tipo_moneda ENUM('D', 'P') NOT NULL,
    cliente_dni BIGINT NOT NULL,
    FOREIGN KEY(cliente_dni) REFERENCES CLIENTES(dni) ON DELETE CASCADE
);

CREATE TABLE MOVIMIENTOS (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    origen VARCHAR(120) NOT NULL,
    destino VARCHAR(120),
    monto FLOAT NOT NULL,
    tipo ENUM('D', 'R', 'T') NOT NULL,
    fecha DATETIME NOT NULL
);

CREATE VIEW vista_cliente_cuenta AS
SELECT c.*, cl.nombre, cl.apellido, cl.dni
FROM CUENTAS c
JOIN CLIENTES cl ON c.cliente_dni = cl.dni