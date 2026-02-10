DROP DATABASE IF EXISTS agenda;
CREATE DATABASE agenda;
USE agenda;

CREATE TABLE Personas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE Telefonos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personaId INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    FOREIGN KEY (personaId) REFERENCES Personas(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Direcciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto_direccion VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE Personas_Direcciones (
    personaId INT NOT NULL,
    direccionId INT NOT NULL,
    PRIMARY KEY (personaId, direccionId),
    FOREIGN KEY (personaId) REFERENCES Personas(id) ON DELETE CASCADE,
    FOREIGN KEY (direccionId) REFERENCES Direcciones(id)
);

CREATE USER IF NOT EXISTS 'usuario1'@'localhost' IDENTIFIED BY 'superpassword';
GRANT ALL PRIVILEGES ON agenda.* TO 'usuario1'@'localhost';
FLUSH PRIVILEGES;
