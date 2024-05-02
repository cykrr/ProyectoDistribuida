CREATE DATABASE IF NOT EXISTS app_tienda;

USE app_tienda;

CREATE TABLE IF NOT EXISTS cajeros (
    idCajero INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    clave INT NOT NULL
);

CREATE TABLE IF NOT EXISTS ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    idCajero INT NOT NULL,
    FOREIGN KEY (idCajero) REFERENCES cajeros(idCajero)
);

CREATE TABLE IF NOT EXISTS productosVenta (
    idVenta INT,
    idProducto INT,
    precio INT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY(idVenta, idProducto),
    FOREIGN KEY (idVenta) REFERENCES ventas(idVenta)
);

CREATE TABLE IF NOT EXISTS stock (
    idProducto INT PRIMARY KEY,
    stock INT NOT NULL
)