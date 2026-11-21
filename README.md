# Seminar Contact API

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 4.1.2](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![Dockerized](https://img.shields.io/badge/Docker-Dockerized-2496ED?logo=docker&logoColor=white)
![Build](https://github.com/eduardogalba/seminar-contacts-api/actions/workflows/build_n_deploy_api.yml/badge.svg?branch=main)

REST API for contact management, developed as part of a cloud deployment seminar. It provides CRUD operations
for contacts, includes request validations, persists data in PostgreSQL, and exposes interactive API
documentation through Swagger.

## Technologies Used

- **Java 21**
- **Spring Boot**, including **Spring Web**, **Spring Data JPA** and **Spring Validation**
- **PostgreSQL**
- **Flyway** for database migrations
- **Springdoc OpenAPI (Swagger)** for API documentation
- **Docker** and **Docker Compose** for containerization and local execution
- **Azure Container Apps** for cloud deployment
- **GitHub Actions** for CI/CD automation

## Automated Deployment (CI/CD)

The project includes the GitHub Actions workflow
`.github/workflows/build_n_deploy_api.yml`. This workflow runs automatically when a `push` is made to the
`main` branch—for changes to the API or the workflow itself—and performs the following tasks:

1. Builds the application's Docker image.
2. Publishes the image to Docker Hub using the commit SHA as its tag.
3. Updates the container deployed in Azure Container Apps to use the new image.

For the deployment to work, configure the credentials and variables used by the workflow in GitHub,
preferably within the `seminar-contacts` environment:

- **Secrets**: `DOCKERHUB_TOKEN`, `AZURE_CREDENTIALS`, and `DB_CONTACT_SERVICE_PASSWORD`.
- **Variables**: `DOCKERHUB_USERNAME`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID`, `AZURE_RESOURCE_GROUP`,
  `CONTAINER_APP_NAME`, `DB_CONTACT_SERVICE_HOST`, `DB_CONTACT_SERVICE_NAME`, and `DB_CONTACT_SERVICE_USER`.

Configure these values under **Settings → Secrets and variables → Actions** in the repository. The Docker Hub
token is used to publish the image, while `AZURE_CREDENTIALS` must contain the credentials for the service
principal used to authenticate with Azure.

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

## Running Locally

Assuming PostgreSQL is available and the environment variables are configured as described in the installation
and configuration section, the API can be started directly with Maven:

```bash
cd api
mvn spring-boot:run
```

The application can also be started with Docker Compose from the repository root:

```bash
docker compose up --build
```

When running directly with Maven, the API is available at `http://localhost:8080`. With Docker Compose, the
default published port is `8081`, so the API is available at `http://localhost:8081`.

## API Documentation (Swagger)

Once the application is running, the interactive Swagger UI is available at:

- Maven: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Docker Compose: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

The OpenAPI document in JSON format is available at `/v3/api-docs`:

- Maven: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- Docker Compose: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

## Installation and Configuration

### Prerequisites

To run the project locally, you need:

- Java 21.
- Maven 3.9 or later when running the application outside Docker.
- Docker and Docker Compose.

### Option 1: Run the entire application with Docker Compose

From the repository root, run:

```bash
docker compose up --build
```

This command starts:

- PostgreSQL 16 on port `5432`.
- The Spring Boot API at `http://localhost:8081`.

The database is created with these values:

```text
Database:      contacts_db
User:          contacts_user
Password:      contacts_pass
Host:          postgres (from the API container)
Port:          5432
```

Flyway migrations run automatically when the API starts. Interactive documentation is available in
[Swagger UI](http://localhost:8081/swagger-ui/index.html).

To stop the containers:

```bash
docker compose down
```

To preserve PostgreSQL data, do not add `-v` to the command above.

### Option 2: Run the API with Maven

This option uses Docker only for PostgreSQL. Start the database first:

```bash
docker compose up -d postgres
```

Then configure the environment variables and run the application from the `api` directory.

On Linux/macOS:

```bash
export DB_CONTACT_SERVICE_HOST=localhost
export DB_CONTACT_SERVICE_PORT=5432
export DB_CONTACT_SERVICE_NAME=contacts_db
export DB_CONTACT_SERVICE_USER=contacts_user
export DB_CONTACT_SERVICE_PASSWORD=contacts_pass

cd api
mvn spring-boot:run
```

On PowerShell:

```powershell
$env:DB_CONTACT_SERVICE_HOST = "localhost"
$env:DB_CONTACT_SERVICE_PORT = "5432"
$env:DB_CONTACT_SERVICE_NAME = "contacts_db"
$env:DB_CONTACT_SERVICE_USER = "contacts_user"
$env:DB_CONTACT_SERVICE_PASSWORD = "contacts_pass"

Set-Location api
mvn spring-boot:run
```

In this mode, the API is available at `http://localhost:8080` and Swagger UI at
`http://localhost:8080/swagger-ui/index.html`.

### Environment Variables

The PostgreSQL connection is configured in `api/src/main/resources/application.yml` using the following
environment variables:

| Variable | Required | Default value | Description |
|---|---:|---|---|
| `DB_CONTACT_SERVICE_HOST` | No | `localhost` | PostgreSQL host. |
| `DB_CONTACT_SERVICE_PORT` | No | `5432` | PostgreSQL port. |
| `DB_CONTACT_SERVICE_NAME` | No | `contacts_db` | Database name. |
| `DB_CONTACT_SERVICE_USER` | Yes | — | PostgreSQL user. |
| `DB_CONTACT_SERVICE_PASSWORD` | Yes | — | PostgreSQL password. |

With Docker Compose, these variables are provided automatically to the API container. When running the
application directly with Maven, define them in the environment beforehand as shown above.

Hibernate uses `ddl-auto: validate`, so it does not create or modify tables. Flyway applies the migrations
located in `api/src/main/resources/flyway/` during startup.
