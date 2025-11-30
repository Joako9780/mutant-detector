# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

# --- ESTA LÍNEA ES LA QUE ARREGLA EL ERROR ---
RUN chmod +x mvnw
# ---------------------------------------------

RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
# Asegúrate de que el nombre coincide con el artifactId y version de tu pom.xml
COPY --from=build /app/target/mutant-detector-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]