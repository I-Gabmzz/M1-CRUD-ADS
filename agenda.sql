CREATE DATABASE IF NOT EXISTS agenda;
USE agenda;

CREATE USER IF NOT EXISTS 'usuario1'@'localhost' IDENTIFIED BY 'superpassword';
GRANT ALL PRIVILEGES ON agenda.* TO 'usuario1'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS Personas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS Telefonos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personaId INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    FOREIGN KEY (personaId) REFERENCES Personas(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

INSERT INTO Personas (nombre, direccion) VALUES ('John Doe' , 'Calle Falsa 123');

INSERT INTO Telefonos (personaId, telefono) VALUES (1, '555-7654321');
INSERT INTO Telefonos (personaId, telefono) VALUES (1, '666-1234567');

SELECT * FROM Personas;
SELECT * FROM Telefonos;
