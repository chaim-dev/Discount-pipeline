FROM openjdk:17-jdk

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY target/discount-0.0.1-SNAPSHOT.jar /app/discount.jar

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application Java
CMD ["java", "-jar", "/app/discount.jar"]
