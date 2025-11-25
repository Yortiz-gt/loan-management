# Sistema de Gestión de Préstamos

Este documento proporciona las instrucciones necesarias para configurar y ejecutar el proyecto de gestión de préstamos.

## Prerrequisitos

Asegúrate de tener instaladas las siguientes herramientas:
- **Java 17**: El proyecto está construido con Java 17.
- **Maven**: Para la gestión de dependencias y la construcción del proyecto.
- **Docker**: (Opcional, pero recomendado) Para ejecutar la base de datos y la aplicación en contenedores.

## Configuración

La configuración de la aplicación se encuentra en `src/main/resources/application.properties`.

### Base de Datos
- **Driver**: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
- **URL**: `jdbc:sqlserver://localhost:1433;databaseName=LoanDB;encrypt=true;trustServerCertificate=true`
- **Username**: El nombre de usuario se configura a través de la variable de entorno `USR`.
- **Password**: La contraseña se configura a través de la variable de entorno `PWD`.

La propiedad `spring.jpa.hibernate.ddl-auto=update` indica que Hibernate actualizará el esquema de la base de datos automáticamente al iniciar la aplicación.

## Inicialización de la Base de Datos

El directorio `sql-init` contiene el script `create-tables.sql` y `insert-data.sql`  que se utiliza para inicializar la base de datos. Este script realiza las siguientes acciones:

1.  **Crea la base de datos `LoanManagementDB` si no existe.**
2.  **Crea las tablas necesarias:**
    - `EstadoSolicitud`
    - `TipoPlazo`
    - `Cliente`
    - `SolicitudPrestamo`
    - `Prestamo`
    - `Pago`
3.  **Inserta datos iniciales en las tablas `EstadoSolicitud` y `TipoPlazo`.**

Si no utilizas Docker Compose, puedes ejecutar este script manualmente en tu instancia de SQL Server para preparar la base de datos antes de iniciar la aplicación.

## Cómo Ejecutar el Proyecto

### Usando Docker Compose (Recomendado)

El proyecto incluye un archivo `compose.yaml` para facilitar la ejecución.

1.  **Configura las variables de entorno**:
    Crea un archivo `.env` en la raíz del proyecto con el siguiente contenido:
    ```
    USR=sa
    PWD=verYs3cret
    ```
    *Nota: Reemplaza `verYs3cret` con la contraseña que desees para la base de datos.*

2.  **Ejecuta Docker Compose**:
    En la raíz del proyecto, ejecuta el siguiente comando:
    ```bash
    docker-compose up --build
    ```
    Esto construirá la imagen de la aplicación y levantará los contenedores para la aplicación y la base de datos SQL Server.

### Usando Maven

1.  **Configura las variables de entorno**:
    Asegúrate de tener una instancia de SQL Server ejecutándose y accesible en `localhost:1433`. Luego, configura las variables de entorno `USR` y `PWD` en tu sistema o en tu IDE.

    **En Windows (PowerShell):**
    ```powershell
    $env:USR="sa"
    $env:PWD="your_password"
    ```

    **En macOS/Linux:**
    ```bash
    export USR=sa
    export PWD=your_password
    ```

2.  **Ejecuta la aplicación**:
    Utiliza el siguiente comando de Maven en la raíz del proyecto:
    ```bash
    mvn spring-boot:run
    ```

## Estructura del Proyecto

El proyecto sigue una arquitectura en capas estándar:

- `src/main/java/com/bank/loan/management`
  - `web`: Contiene los controladores REST para exponer la API.
  - `svc`: Contiene la lógica de negocio de la aplicación.
  - `dao`: Contiene las interfaces de Spring Data JPA para la interacción con la base de datos.
  - `model`: Contiene las entidades JPA que representan las tablas de la base de datos.
- `src/main/resources`: Contiene los archivos de configuración y estáticos.
- `pom.xml`: Define las dependencias y la configuración de construcción del proyecto.
- `sql-init`: Contiene scripts de inicialización para la base de datos.
