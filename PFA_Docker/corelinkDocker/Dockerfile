# ------------ ÉTAPE 1 : BUILD MAVEN -----------------
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests=true -B


# ------------ ÉTAPE 2 : RUNNER DE TESTS -------------
FROM selenium/standalone-chrome:127.0

USER root


# Installer Maven + JDK dans l'image Selenium
RUN apt-get update && apt-get install -y maven openjdk-17-jdk

# Installer Maven dans l’image Selenium
RUN apt-get update && apt-get install -y maven && apt-get clean


WORKDIR /app

# Copier TOUT le projet buildé
COPY --from=build /app /app


# Permissions Selenium

# Copier le script d’attente
COPY wait-for-services.sh /app/wait-for-services.sh
RUN sed -i 's/\r$//' /app/wait-for-services.sh
RUN chmod +x /app/wait-for-services.sh


# Donner accès à seluser
RUN chown -R seluser:seluser /app
USER seluser



CMD ["bash", "/app/wait-for-services.sh"]

