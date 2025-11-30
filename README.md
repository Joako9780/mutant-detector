# Mutant Detector API 🧬

API REST desarrollada en Spring Boot para detectar mutantes basándose en su secuencia de ADN, solicitada por Magneto para reclutar soldados.

## Descripción

El proyecto expone una API que recibe una secuencia de ADN y determina si pertenece a un humano o a un mutante.
Además, persiste los registros verificados en una base de datos H2 para evitar recálculos y ofrece un endpoint de estadísticas.

**Regla de Negocio:**
Un humano es considerado **mutante** si se encuentran **más de una secuencia** de cuatro letras iguales de forma oblicua, horizontal o vertical.

## Tecnologías

* **Java 17**
* **Spring Boot 3** (Web, Data JPA)
* **H2 Database** (Base de datos en memoria)
* **Lombok** (Reducción de código boilerplate)
* **JUnit 5 & MockMvc** (Testing unitario y de integración)
* **Maven** (Gestor de dependencias)

## Instalación y Ejecución

### Prerrequisitos
* JDK 17 instalado.
* Puerto 8080 disponible.

### Pasos
1.  Clonar el repositorio.
2.  Ejecutar el proyecto usando el wrapper de Maven incluido:

**En Linux/Mac:**
./mvnw spring-boot:run

**En Windows:**
.\mvnw.cmd spring-boot:run

La aplicación iniciará en http://localhost:8080.

## Uso de la API

### 1. Detectar Mutante
Envía una secuencia de ADN para su análisis.

* **URL:** /mutant/
* **Método:** POST
* **Respuestas:**
    * 200 OK: Es un Mutante.
    * 403 Forbidden: Es un Humano.
    * 400 Bad Request: Datos de entrada inválidos (Array vacío, caracteres erróneos, etc).

#### Ejemplos de prueba para Importar a Postman

###### **Ejemplo Mutante:**

curl --location 'http://localhost:8080/mutant/' \
--header 'Content-Type: application/json' \
--data '{
"dna": [
"ATGCGA",
"CAGTGC",
"TTATGT",
"AGAAGG",
"CCCCTA",
"TCACTG"
]
}'

###### **Ejemplo Humano:**

curl --location 'http://localhost:8080/mutant/' \
--header 'Content-Type: application/json' \
--data '{
"dna": [
"ATGCGA",
"CAGTGC",
"TTATTT",
"AGACGG",
"GCGTCA",
"TCACTG"
]
}'

### 2. Obtener Estadísticas
Devuelve el conteo de verificaciones y el ratio de mutantes.

* **URL:** /stats
* **Método:** GET
* **Respuesta Exitosa (200 OK):**

{
"count_mutant_dna": 40,
"count_human_dna": 100,
"ratio": 0.4
}

**Comando:**
curl --location 'http://localhost:8080/stats'

## Testing y Cobertura

Para ejecutar los tests automatizados y verificar la lógica de negocio:

./mvnw test o dentro del ID navegar hasta src/test/java/com/mercadolibre/mutant_detector, dar click
derecho sobre el directorio y seleccionar la opción "Run 'Tests in 'mutant_detector'' with Coverage"

### Code Coverage
El proyecto cuenta con una cobertura de código superior al 80%.
Se puede verificar ejecutando los tests con cobertura desde IntelliJ IDEA o IDEs compatibles.

## Base de Datos (H2 Console)

Puedes acceder a la base de datos en memoria para inspeccionar los registros.

1.  Navega a: http://localhost:8080/h2-console
2.  JDBC URL: jdbc:h2:mem:mutantdb
3.  User Name: sa
4.  Password: (dejar vacío)

# Documentación (Swagger UI)
El proyecto incluye documentación interactiva con Swagger UI. Para ver la definición de los endpoints y probarlos directamente desde el navegador:

Inicia la aplicación.

Ingresa a: http://localhost:8080/swagger-ui.html