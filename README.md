# Workflow AI

Enterprise-oriented business management backend built with Java and Spring Boot, with a modular architecture for business workflows and AI-powered capabilities.

The project is being developed as a foundation for a multi-tenant business management SaaS platform, combining secure authentication, business management modules, MongoDB persistence, and AI capabilities.

## Tech Stack

- Java 17
- Spring Boot 3.5.16
- Spring Web
- Spring Data MongoDB
- Spring Security
- JWT
- MongoDB
- Spring Validation
- Spring Mail
- OpenAPI / Swagger
- Lombok
- Maven

## Core Capabilities

- RESTful backend APIs
- MongoDB-based data persistence
- Authentication and security with Spring Security
- JWT-based authentication
- Request validation
- Email integration
- API documentation with OpenAPI / Swagger
- Development support with Spring Boot DevTools

## Architecture

The application follows a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MongoDB

src/
├── main/
│   ├── java/com/rohit/workflow_ai/
│   │   ├── activity/
│   │   ├── ai/
│   │   ├── auth/
│   │   ├── client/
│   │   ├── common/
│   │   ├── company/
│   │   ├── config/
│   │   ├── dashboard/
│   │   ├── exception/
│   │   ├── project/
│   │   ├── security/
│   │   ├── task/
│   │   ├── user/
│   │   └── WorkflowAiApplication.java
│   │
│   └── resources/
│
└── test/
    └── java/com/rohit/workflow_ai/
