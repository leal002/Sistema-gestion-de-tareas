# Sistema de Gestión de Tareas

## Descripción

El **Sistema de Gestión de Tareas** es una aplicación web desarrollada utilizando **Spring Boot** para el backend, **PostgreSQL** como base de datos, y **HTML, CSS, y JavaScript** para el frontend. El sistema permite a los usuarios gestionar sus tareas, realizando operaciones como crear, editar, eliminar y ver tareas. Además, incluye un sistema de autenticación para registrar y autenticar usuarios.

Este sistema es parte de un proyecto de gestión de tareas en el contexto de una empresa, facilitando la administración y seguimiento de actividades diarias.

## Tecnologías utilizadas

### Backend:
- **Spring Boot**: Framework para el desarrollo del backend.
- **PostgreSQL**: Base de datos relacional para almacenar los datos del sistema.
- **Spring Security**: Para la autenticación y autorización de usuarios.
- **Lombok**: Biblioteca para reducir el código repetitivo en las clases de modelo.

### Frontend:
- **HTML/CSS**: Para la estructura y el diseño de la página web.
- **JavaScript**: Para la lógica y la interacción con el backend mediante solicitudes HTTP.

### Testing:
- **JUnit y Mockito**: Para las pruebas unitarias y de integración del backend.
- **Postman**: Para pruebas manuales de los endpoints del API REST.

## Características

- **Autenticación de usuarios**: Permite a los usuarios registrarse y acceder al sistema mediante credenciales.
- **Gestión de tareas**: Los usuarios pueden crear, editar, eliminar y ver tareas.
- **Interfaz de usuario**: Interfaz web interactiva para gestionar tareas de forma sencilla.
- **Validación de datos**: El sistema valida los datos en las solicitudes para garantizar la integridad de la información.

## Requisitos

- **JDK 11 o superior**
- **Maven** o **Gradle** (dependiendo del sistema de construcción utilizado)
- **PostgreSQL**: Base de datos local o remota para almacenar los datos.

## Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu_usuario/gestion-de-tareas.git
