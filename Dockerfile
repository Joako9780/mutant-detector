# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

# Permisos para el wrapper
RUN chmod +x mvnw

# --- CAMBIO AQUÍ: Agregamos -Dfile.encoding=UTF-8 ---
RUN ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8
# ----------------------------------------------------

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/mutant-detector-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]