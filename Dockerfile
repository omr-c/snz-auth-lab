# ETAPA 1: Compilación (La cocina de preparación)
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copiamos el pom y el código para compilar
COPY pom.xml .
COPY src ./src
# Construimos el .jar saltándonos los tests para ir más rápido
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (La mesa de servicio)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Solo nos traemos el resultado de la etapa anterior (el .jar)
# Esto hace que la imagen sea súper ligera
COPY --from=build /app/target/app.jar app.jar

# Exponemos el puerto y ejecutamos
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]