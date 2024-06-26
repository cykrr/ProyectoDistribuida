DROP DATABASE IF EXISTS app_tienda;

CREATE DATABASE IF NOT EXISTS app_tienda;

USE app_tienda;

CREATE TABLE IF NOT EXISTS usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    clave INT NOT NULL,
    rol INT NOT NULL
);

CREATE TABLE IF NOT EXISTS boletas (
    idBoleta INT AUTO_INCREMENT PRIMARY KEY,
    nombreCajero VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS itemsBoleta (
    idBoleta INT,
    idProducto INT,
    precioTotal INT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY(idBoleta, idProducto),
    FOREIGN KEY (idBoleta) REFERENCES boletas(idBoleta)
);

CREATE TABLE IF NOT EXISTS stock (
    idProducto INT PRIMARY KEY,
    stock INT NOT NULL
);


INSERT IGNORE INTO usuarios
    (nombre, clave, rol)
VALUES
    ('Juan', 11111, 0),
    ('Pepito', 22222, 1),
    ('Miguel', 33333, 0);

INSERT IGNORE INTO boletas
    (idBoleta, nombreCajero)
VALUES
    (1, 'Juan'),
    (2, 'Juan');

INSERT IGNORE INTO itemsBoleta
    (idBoleta, idProducto, precioTotal, cantidad)
VALUES
    (1, 1, 3000, 1),
    (1, 5, 5000, 2),
    (1, 7, 2000, 1),
    (2, 2, 2000, 1),
    (2, 3, 6400, 2);

INSERT IGNORE INTO stock
    (idProducto, stock)
VALUES
    (1, 25),
    (2, 28),
    (3, 23),
    (4, 30),
    (5, 41),
    (6, 17),
    (7, 15),
    (8, 23),
    (9, 26),
    (10, 28)