# Étape 1 : Construire l'application avec Maven
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le pom.xml et les autres fichiers nécessaires à la construction de l'application
COPY pom.xml ./
COPY src ./src
EXPOSE 8080

# Exécuter Maven pour construire l'application sans tests
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image OpenJDK légère pour exécuter l'application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier uniquement le fichier JAR généré depuis l'étape de build
COPY --from=build /app/target/auth-0.0.1-SNAPSHOT.jar /app/auth.jar

# Spécifier la commande d'exécution
ENTRYPOINT ["java", "-jar", "/app/auth.jar" , "--server.port=8080"]