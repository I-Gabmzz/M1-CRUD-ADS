# CRUD-ADS (Meta 1.3) 

Sistema de gestión de contactos desarrollado en **Java** y **JavaFX**, hecho bajo una arquitectura de software para cumplir con los principios de **Alta Cohesión** y **Bajo Acoplamiento**.

Esta versión mantiene la funcionalidad del CRUD completo (Personas, Teléfonos y Direcciones) de la Meta 1.2, pero reestructura completamente el código interno aplicando los **5 Principios SOLID**.

---
### 💎 Principios SOLID Aplicados

-   **S (SRP):** Cada clase tiene una única responsabilidad (Vista, Lógica o Datos).
-   **O (OCP):** Implementación de la interfaz `InterfazConexion`, permitiendo cambiar el motor de base de datos sin modificar el código existente.
-   **L (LSP):** La implementación `PersonaDAO` respeta fielmente el contrato de `InterfazPersonaDAO`, garantizando la estabilidad del sistema.
-   **I (ISP):** Segregación de interfaces en `InterfazLecturaDeDatos` y `InterfazEscrituraDeDatos` para no forzar dependencias innecesarias.
-   **D (DIP):** Inyección de dependencias en el Controlador y la Lógica, eliminando el acoplamiento directo a clases concretas.

---

## ✨ Funcionalidades

-   **Gestión de Personas**: Alta, búsqueda, modificación y eliminación con validaciones robustas.
-   **Gestión de Teléfonos**: Relación **1:N** optimizada.
-   **Gestión de Direcciones**: Relación **N:M** con reutilización inteligente de direcciones compartidas.

---

## 🧰 Tecnologías

-   **Lenguaje:** Java (JDK 17+)
-   **Interfaz:** JavaFX
-   **Arquitectura:** MVC / Capas (Layered Architecture)
-   **Base de Datos:** MariaDB
-   **Gestión de Dependencias:** Maven
-   **Pruebas:** JUnit 5

---

## ✅ Requisitos

-   **JDK** instalado (versión 17 o superior).
-   **MariaDB Server** instalado y corriendo en el puerto 3306.
-   Cliente SQL para ejecutar el script inicial.

---

## 🚀 Instalación y ejecución

### 1. Preparar la Base de Datos

### 2. Clonar el repositorio
