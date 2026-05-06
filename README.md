# 🧪 Consigna Técnica – ABM de Historial Médico

## 🧾 Contexto
Estás participando en el desarrollo de una aplicación médica que ya cuenta con un sistema funcional de ABM de usuarios.  
El frontend está desarrollado en React, el backend en Java con Spring Boot, y la base de datos utilizada es MySQL.

---

## 🎯 Objetivo
Agregar una nueva funcionalidad que permita gestionar un ABM (Alta, Baja, Modificación y Listado) de historiales médicos asociados a los usuarios (pacientes).  

Este historial incluye información clínica importante como enfermedades crónicas, alergias y antecedentes familiares.

---

## ✅ Requisitos funcionales

### 1. Modelo MedicalHistory
Crear un nuevo modelo llamado **MedicalHistory** con los siguientes campos:

| Campo               | Tipo   | Descripción                                                                 |
|--------------------|--------|-----------------------------------------------------------------------------|
| id                 | Long   | Identificador único (autogenerado)                                          |
| user_id            | Long   | ID del usuario (paciente) al que pertenece el historial                     |
| chronic_conditions | String | Enfermedades crónicas (ej: diabetes, hipertensión, etc.)                    |
| allergies          | String | Alergias (ej: penicilina, gluten, etc.)                                     |
| past_surgeries     | String | Cirugías previas (descripción general)                                      |
| family_history     | String | Historial médico familiar (antecedentes genéticos)                          |
| notes              | String | Notas adicionales del profesional médico                                    |

---

## ⚙️ Backend (Java + Spring Boot + MySQL)

Crear la entidad **MedicalHistory** y mapearla a una tabla en MySQL.  

Establecer una relación `@ManyToOne` con la entidad **User**.

### Endpoints a implementar:

| Método | Ruta                               | Descripción                                      |
|--------|------------------------------------|--------------------------------------------------|
| GET    | /medical-history                   | Listar todos los historiales médicos             |
| GET    | /medical-history/{id}              | Obtener un historial médico específico           |
| GET    | /users/{userId}/medical-history    | Obtener el historial médico de un usuario        |
| POST   | /medical-history                   | Crear un nuevo historial médico                  |
| PUT    | /medical-history/{id}              | Actualizar un historial existente                |
| DELETE | /medical-history/{id}              | Eliminar un historial médico                     |

---

## 🖥️ Frontend (React)

Implementar en React una sección para gestionar el historial médico de los usuarios.

### 1. Listado
Vista de tabla que muestre los historiales médicos asociados a un usuario seleccionado.  

Botón para crear nuevo historial.  

Botón de editar y eliminar en cada fila.

---

### 2. Formulario de creación y edición

Campos para ingresar:
- Enfermedades crónicas  
- Alergias  
- Cirugías previas  
- Antecedentes familiares  
- Notas  

Validaciones básicas (campos requeridos, longitud, etc.).  

Conexión con la API REST para crear o actualizar.

---

### 3. Eliminación
Confirmación antes de eliminar.  

Llamada a `DELETE /medical-history/{id}`

---

### 4. Integración con el usuario
La vista debe estar asociada al usuario seleccionado (por ejemplo, desde `/users/:id/medical-history`).  

Llamar a `GET /users/{userId}/medical-history` para traer el historial correspondiente.

---

### 5. Manejo de errores y loading
Indicadores de carga (spinners).  

Mensajes de éxito y error (ej. toast, alert, etc.).
