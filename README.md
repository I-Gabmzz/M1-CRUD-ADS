# CRUD-ADS (Meta 1.2)

Sistema de gestión de contactos desarrollado en **Java** utilizando **JavaFX** para la interfaz gráfica y **MariaDB** como base de datos.

Esta versión implementa un **CRUD completo (Altas, Bajas, Cambios y Consultas)** con manejo avanzado de bases de datos relacionales:
* **Uno a Muchos (1:N):** Una persona puede tener múltiples teléfonos asociados.
* **Muchos a Muchos (N:M):** Una persona puede tener múltiples direcciones y **varias personas pueden compartir la misma dirección**.

---

## ✨ Funcionalidades

- **Gestión de Personas**: Alta, búsqueda por ID, modificación de nombre y eliminación en cascada.
- **Gestión de Teléfonos**: Agregar y eliminar múltiples números por contacto.
- **Gestión de Direcciones (Nuevo en Meta 1.2)**:
  - Vincular múltiples direcciones a una persona.
  - **Direcciones Compartidas**: El sistema detecta si una dirección ya existe y la reutiliza, vinculando a las personas mediante una tabla intermedia.
  - Eliminación inteligente de vínculos.

---

## 🧰 Tecnologías

- **Lenguaje:** Java (JDK 17+)
- **Interfaz:** JavaFX
- **Base de Datos:** MariaDB
- **Gestión de Dependencias:** Maven
- **Pruebas:** JUnit 5

---

## ✅ Requisitos

- **JDK** instalado (versión 17 o superior).
- **MariaDB Server** instalado y corriendo en el puerto 3306.
- Cliente SQL para ejecutar el script inicial.

---

## 🚀 Instalación y ejecución

### 1. Realizar el script de la agenda en el cliente SQL
### 2. Clonar el repositorio
