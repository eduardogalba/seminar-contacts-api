# ============================================================
# Etapa 1: Build
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# 1. Copiamos solo el pom.xml primero.
#    Esta capa se cachea hasta que cambien las dependencias.
COPY pom.xml .
COPY checkstyle.xml .

# 2. Descargamos dependencias. Usamos un cache mount de BuildKit
#    para no re-descargar todo en cada build local.
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

# 3. Ahora copiamos el código fuente (lo que cambia a menudo).
COPY src ./src

# 4. Compilamos el JAR (sin tests para acelerar).
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B

# ============================================================
# Etapa 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar

ARG VERSION=dev
ENV APP_VERSION=$VERSION

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]