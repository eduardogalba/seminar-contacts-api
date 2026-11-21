# Seminar Contacts API

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 4.1.2](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![Dockerized](https://img.shields.io/badge/Docker-Dockerized-2496ED?logo=docker&logoColor=white)
![Build](https://github.com/eduardogalba/seminar-contacts-api/actions/workflows/build_n_deploy_api.yml/badge.svg?branch=main)

REST API for contact management, developed as part of a cloud deployment seminar. It provides CRUD operations
for contacts, includes request validations, persists data in PostgreSQL, and exposes interactive API
documentation through Swagger.

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot**, including **Spring Web**, **Spring Data JPA** and **Spring Validation**
- **PostgreSQL**
- **Flyway** for database migrations
- **Springdoc OpenAPI (Swagger)** for API documentation
- **Docker** and **Docker Compose** for containerization and local execution
- **Azure Container Apps** for cloud deployment
- **GitHub Actions** for CI/CD automation

## Despliegue Automático (CI/CD)

El proyecto incluye el workflow de GitHub Actions
`.github/workflows/build_n_deploy_api.yml`. Este workflow se ejecuta automáticamente cuando se realiza un
`push` a la rama `main` —para cambios en la API o en el propio workflow— y realiza las siguientes tareas:

1. Construye la imagen Docker de la aplicación.
2. Publica la imagen en Docker Hub usando el SHA del commit como etiqueta.
3. Actualiza el contenedor desplegado en Azure Container Apps para utilizar la nueva imagen.

Para que el despliegue funcione, es necesario configurar en GitHub, preferiblemente dentro del environment
`seminar-contacts`, las credenciales y variables utilizadas por el workflow:

- **Secretos**: `DOCKERHUB_TOKEN`, `AZURE_CREDENTIALS` y `DB_CONTACT_SERVICE_PASSWORD`.
- **Variables**: `DOCKERHUB_USERNAME`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID`, `AZURE_RESOURCE_GROUP`,
  `CONTAINER_APP_NAME`, `DB_CONTACT_SERVICE_HOST`, `DB_CONTACT_SERVICE_NAME` y `DB_CONTACT_SERVICE_USER`.

Estos valores se configuran en **Settings → Secrets and variables → Actions** del repositorio. El token de
Docker Hub se utiliza para publicar la imagen y `AZURE_CREDENTIALS` debe contener las credenciales del service
principal utilizado para autenticarse contra Azure.

If you are new to the repository, this section gives you a quick tour of what it does and how it is put
together.

## What this project does

The API exposes CRUD operations over a single resource: **contacts**. A contact has a first name, an optional
last name, a list of phone numbers, a list of email addresses, an optional postal address and an optional birth
date. Contacts are stored in PostgreSQL and the schema is created and evolved through Flyway migrations, so
the database is never modified by hand.

The application is built with **Spring Boot 4** and **Java 21**, follows a **hexagonal (ports and adapters)
architecture**, and is designed to run both locally (with Docker Compose) and in the cloud (on Azure Container
Apps).

## Why this project exists

This repository is the reference implementation for a seminar on deploying applications to the cloud. The
goal is not to build a feature-rich product, but to demonstrate, end to end, how a Spring Boot service is:

- structured around a clean domain model,
- persisted in a relational database with versioned migrations,
- packaged as a Docker image,
- validated by a CI pipeline on every change,
- and deployed to a managed cloud platform with a CD pipeline.

Every design decision in the project is meant to be readable and reproducible by someone following the
seminar, rather than optimised for a real production workload.

## How it is organised

The code is split into three layers, each with a clear responsibility:

- **`domain`** — the `Contact` record and the outbound port (`ContactRepositoryPort`). It has no framework
  dependencies.
- **`application`** — the use cases (`ManageContactsUseCase`) that orchestrate the domain.
- **`infrastructure`** — the adapters: a REST controller with OpenAPI-generated DTOs on the inbound side, and
  a JPA entity with a Spring Data repository on the outbound side.

The database schema is managed by **Flyway**, with migration scripts under
`src/main/resources/flyway/releases/`. Hibernate runs in `validate` mode, so it only checks that the entities
match the schema — it never creates or alters tables.

## Ejecución en Local

Asumiendo que PostgreSQL está disponible y que las variables de entorno están configuradas según la sección
de instalación y configuración, la API puede iniciarse directamente con Maven:

```bash
cd api
mvn spring-boot:run
```

También se puede levantar la aplicación usando Docker Compose desde la raíz del repositorio:

```bash
docker compose up --build
```

En ambos casos, la API queda disponible en `http://localhost:8080` cuando se ejecuta directamente con Maven.
Con Docker Compose, el puerto publicado por defecto es `8081`, por lo que se accede mediante
`http://localhost:8081`.

## Documentación de la API (Swagger)

Una vez levantada la aplicación, la interfaz interactiva de Swagger UI está disponible en:

- Maven: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Docker Compose: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

El documento OpenAPI en formato JSON se encuentra en `/v3/api-docs`:

- Maven: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- Docker Compose: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

## Instalación y Configuración

### Requisitos previos

Para ejecutar el proyecto localmente se necesita:

- Java 21.
- Maven 3.9 o superior si se ejecuta la aplicación fuera de Docker.
- Docker y Docker Compose.

### Opción 1: levantar toda la aplicación con Docker Compose

Desde la raíz del repositorio, ejecuta:

```bash
docker compose up --build
```

Este comando inicia:

- PostgreSQL 16 en el puerto `5432`.
- La API Spring Boot en `http://localhost:8081`.

La base de datos se crea con estos valores:

```text
Base de datos: contacts_db
Usuario:       contacts_user
Contraseña:    contacts_pass
Host:          postgres (desde el contenedor de la API)
Puerto:        5432
```

Las migraciones de Flyway se ejecutan automáticamente al iniciar la API. La documentación interactiva está
disponible en [Swagger UI](http://localhost:8081/swagger-ui/index.html).

Para detener los contenedores:

```bash
docker compose down
```

Para conservar los datos de PostgreSQL, no se debe añadir `-v` al comando anterior.

### Opción 2: ejecutar la API con Maven

Esta opción utiliza Docker únicamente para PostgreSQL. Primero inicia la base de datos:

```bash
docker compose up -d postgres
```

Después, configura las variables de entorno y ejecuta la aplicación desde la carpeta `api`.

En Linux/macOS:

```bash
export DB_CONTACT_SERVICE_HOST=localhost
export DB_CONTACT_SERVICE_PORT=5432
export DB_CONTACT_SERVICE_NAME=contacts_db
export DB_CONTACT_SERVICE_USER=contacts_user
export DB_CONTACT_SERVICE_PASSWORD=contacts_pass

cd api
mvn spring-boot:run
```

En PowerShell:

```powershell
$env:DB_CONTACT_SERVICE_HOST = "localhost"
$env:DB_CONTACT_SERVICE_PORT = "5432"
$env:DB_CONTACT_SERVICE_NAME = "contacts_db"
$env:DB_CONTACT_SERVICE_USER = "contacts_user"
$env:DB_CONTACT_SERVICE_PASSWORD = "contacts_pass"

Set-Location api
mvn spring-boot:run
```

En este modo, la API queda disponible en `http://localhost:8080` y Swagger UI en
`http://localhost:8080/swagger-ui/index.html`.

### Variables de entorno

La configuración de conexión a PostgreSQL se define en `api/src/main/resources/application.yml` mediante
estas variables:

| Variable | Obligatoria | Valor por defecto | Descripción |
|---|---:|---|---|
| `DB_CONTACT_SERVICE_HOST` | No | `localhost` | Host de PostgreSQL. |
| `DB_CONTACT_SERVICE_PORT` | No | `5432` | Puerto de PostgreSQL. |
| `DB_CONTACT_SERVICE_NAME` | No | `contacts_db` | Nombre de la base de datos. |
| `DB_CONTACT_SERVICE_USER` | Sí | — | Usuario de PostgreSQL. |
| `DB_CONTACT_SERVICE_PASSWORD` | Sí | — | Contraseña de PostgreSQL. |

En Docker Compose, estas variables ya se proporcionan automáticamente al contenedor de la API. Si se ejecuta
la aplicación directamente con Maven, deben definirse previamente en el entorno, como se muestra arriba.

Hibernate utiliza `ddl-auto: validate`, por lo que no crea ni modifica tablas. Flyway aplica las migraciones
ubicadas en `api/src/main/resources/flyway/` durante el arranque.
