# --- Fase 1: Construcción ---
# Usamos una imagen de Maven con Java 17 para construir nuestro proyecto.
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo de configuración de Maven
COPY pom.xml .

# Descargamos todas las dependencias
RUN mvn dependency:go-offline

# Copiamos todo el código fuente
COPY src ./src

# Construimos el archivo .jar
RUN mvn package -DskipTests

# --- Fase 2: Ejecución ---
# Usamos una imagen mucho más ligera de Eclipse Temurin con Java 17 para ejecutar la app.
FROM eclipse-temurin:17-jdk-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar que construimos en la fase anterior
COPY --from=build /app/target/api-server-0.0.1-SNAPSHOT.jar .

# Exponemos el puerto en el que correrá la aplicación
# Render usa el puerto 10000 por defecto, así que no es estrictamente necesario, pero es una buena práctica.
EXPOSE 10000

# El comando para arrancar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "api-server-0.0.1-SNAPSHOT.jar"]