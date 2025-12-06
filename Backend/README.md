# Energy Management System — Backend

The backend of the **Energy Management System (EMS)** is a **microservices-based architecture** built with **Spring Boot**.  
It consists of multiple stateless microservices that handle authentication, user data, and device management, all orchestrated through an API Gateway (Traefik) and Docker Compose.

## Microservices Overview

| Microservice               | Responsibility                                                                           | Access                           |
|----------------------------|------------------------------------------------------------------------------------------|----------------------------------|
| **Authentication Service** | User registration, login, password encryption, JWT generation, role/authority management | API Gateway only                 |
| **User Service**           | Store and manage user profile information (email, phone, address, preferences)           | API Gateway only                 |
| **Device Service**         | Store and manage devices, associate devices with users via data duplication              | API Gateway only                 |
| **Monitoring Service**     | Store and manage sensor readings associated to devices via data duplication              | API Gateway or sensor-data-queue |

## Features

- Stateless Spring Boot microservices
- PostgreSQL persistence for each service
- JWT-based authentication handled at the API Gateway
- Role-based access control (RBAC)
- OpenAPI / Swagger documentation per service
- Docker container support & Docker Compose orchestration
- API Gateway (Traefik) handles JWT validation, CORS, and route mapping
- Internal service communication only (no direct external access)

## Tech Stack

| Component      | Technology                      |
|----------------|---------------------------------|
| Language       | Java 21                         |
| Framework      | Spring Boot                     |
| Build Tool     | Maven                           |
| Database       | PostgreSQL (per service)        |
| Security       | Spring Security, JWT            |
| Documentation  | Springdoc OpenAPI               |
| Container      | Docker                          |
| Deployment     | Docker Compose                  |
| API Gateway    | Traefik with JWT & CORS plugins |
| Message broker | RabbitMQ                        |

## Architecture

The Energy Management System backend is designed for stateless, REST-based, synchronized microservices:

1. **Data consistency** - RabbitMQ queues and topics
   - `energy-management-exchange` - topic that ensures the data the database of each microservice is partially consistent with the others
   - `sensor-data-queue` - queue that provides a public entrypoint that sensors/emulators may access to register power consumption recordings to devices belonging to the `device-microservice-database`
2. **Service isolation** - each microservice has its own database and manages its own entities:  
   - Auth: Users & Roles  
   - User: Profile data  
   - Device: Devices & duplicated user data
   - Monitoring: Readings coming from a sensor

3. **API Gateway (Traefik)**:  
   - Handles JWT validation - dynamic jwt middleware: `github.com/agilezebra/jwt-middleware`
   - Routes requests to appropriate services  
   - Manages CORS policies - CORS middleware
   - Ensures no direct public access to services


