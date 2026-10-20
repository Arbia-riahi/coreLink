# 🏗️ Étape 1 : Construction et tests
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le pom.xml et les sources
COPY pom.xml .
COPY src ./src

# Compiler le projet sans relancer les tests
RUN mvn clean package -DskipTests


# 🏃 Étape 2 : Exécution du backend
FROM eclipse-temurin:17-jre

WORKDIR /app

# Crée les dossiers d’uploads (hors du JAR)
RUN mkdir -p /app/uploads/files /app/uploads/audio

# Copier le JAR généré depuis la phase "build"
COPY --from=build /app/target/CoreLinkByRIAHI.jar .

# Exposer le port
EXPOSE 8080

# Exécuter l’application avec le profil "prod"
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "CoreLinkByRIAHI.jar"]
