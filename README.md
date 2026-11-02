# Contacts Service API

A small, production-style REST API for managing contacts, built as part of an academic seminar on cloud
deployment. The project is intentionally simple in scope but complete in its architecture: it includes a
domain model, a persistence layer backed by PostgreSQL, database migrations managed by Flyway, a REST layer
documented with OpenAPI, and a CI/CD pipeline that builds a Docker image and deploys it to Azure Container
Apps.

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

## How to run it

The fastest way to try the API is with Docker Compose:

Then open http://localhost:8080/swagger-ui.html to explore the endpoints interactively.

For the full setup — environment variables, database migrations, Docker image, CI/CD pipeline and Azure
deployment — see the sections below.

```bash
docker compose up -d
./mvnw spring-boot:run
