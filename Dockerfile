# Usa una imagen base de OpenJDK para Java 17
FROM eclipse-temurin:17-jdk

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
# Asegúrate de que el nombre del JAR coincida con el que genera tu build de Maven
ARG JAR_FILE=target/loan.management-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expone el puerto en el que se ejecuta tu aplicación Spring Boot
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
