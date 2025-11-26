# Sistema de Gestión de Préstamos (Loan Management System)

Este proyecto es un sistema de gestión de préstamos desarrollado con Spring Boot, diseñado para administrar clientes, solicitudes de préstamos, préstamos aprobados y pagos asociados. Se ha construido siguiendo principios de Clean Code, Responsabilidad Única (SRP) y Don't Repeat Yourself (DRY) para asegurar un código mantenible, escalable y robusto.

## Características Principales

*   **Gestión de Clientes:** CRUD completo para la administración de la información de los clientes, incluyendo eliminación en cascada de sus préstamos y solicitudes.
*   **Gestión de Solicitudes de Préstamos:** Creación, consulta, aprobación y rechazo de solicitudes de préstamos, con validaciones de estado y creación automática de préstamos al aprobar.
*   **Gestión de Préstamos:** Consulta de préstamos aprobados y cálculo de saldos pendientes.
*   **Gestión de Pagos:** Registro de pagos, validación de montos y actualización automática del monto pendiente de los préstamos.
*   **Manejo de Excepciones:** Respuestas claras y específicas para errores comunes (ej. recurso no encontrado, cliente ya existente, estado inválido).
*   **Paginación:** Implementación de paginación configurable (tamaño por defecto y máximo) y 1-basada para listados de recursos.
*   **Consulta de Catálogos:** Endpoint para consultar tipos de plazo disponibles.
*   **Pruebas Unitarias:** Cobertura de pruebas exhaustiva para la lógica de negocio crítica en la capa de servicios.
*   **Reporte de Cobertura de Código:** Integración con JaCoCo para generar informes de cobertura.
*   **Docker Compose:** Configuración automatizada y consistente del entorno de desarrollo (base de datos SQL Server y aplicación Spring Boot) con un solo comando.

## Tecnologías Utilizadas

*   **Java 17:** Lenguaje de programación.
*   **Spring Boot 3.2.5:** Framework principal para el desarrollo de la aplicación.
*   **Spring Data JPA:** Para la persistencia de datos y la interacción con la base de datos.
*   **Maven:** Herramienta de gestión de proyectos y construcción.
*   **SQL Server:** Base de datos relacional utilizada.
*   **Lombok:** Para reducir el boilerplate code (getters, setters, constructores, etc.).
*   **JUnit 5 & Mockito:** Para la escritura de pruebas unitarias.
*   **JaCoCo:** Para la generación de informes de cobertura de código.
*   **Jakarta Validation:** Para la validación de datos de entrada.
*   **Docker & Docker Compose:** Para la contenerización y orquestación del entorno de desarrollo.

## Configuración y Ejecución del Proyecto

Sigue estos pasos para poner en marcha el proyecto en tu entorno local.

### Prerrequisitos

Asegúrate de tener instalado lo siguiente:

*   **Java Development Kit (JDK) 17 o superior**
*   **Apache Maven 3.6.0 o superior**
*   **Docker Desktop** (incluye Docker Engine y Docker Compose)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/loan-management-maven.git
cd loan-management-maven/loan.management/loan.management
```

### 2. Construir el Proyecto (Generar el JAR)

Antes de usar Docker Compose, necesitas construir el archivo JAR de tu aplicación. Navega a la raíz del proyecto (`loan.management/loan.management`) y ejecuta Maven:

```bash
mvn clean install -DskipTests
```
**Nota:** Usamos `-DskipTests` aquí para acelerar la construcción del JAR, ya que las pruebas se ejecutarán de nuevo dentro del contenedor si así lo configuras, o puedes ejecutarlas por separado.

### 3. Levantar el Entorno con Docker Compose (Método Recomendado)

Este es el método más sencillo y recomendado para levantar tanto la base de datos como la aplicación, incluyendo la inicialización automática de la base de datos.

Asegúrate de estar en la raíz del proyecto (`loan.management/loan.management`) donde se encuentran `compose.yaml`, `Dockerfile`, `Dockerfile.init-db` y `run-init-scripts.sh`.

```bash
docker compose up --build
```

*   `--build`: Reconstruye la imagen de la aplicación Spring Boot y el inicializador de DB cada vez, asegurando que siempre uses las últimas versiones.
*   **Proceso Automático:**
    1.  El contenedor de SQL Server (`db`) se iniciará.
    2.  El contenedor `init-db` esperará a que SQL Server esté listo y luego ejecutará automáticamente los scripts SQL (`01-create-database.sql`, `02-create-tables.sql`, `03-insert-initial-data.sql`) para crear la base de datos, las tablas y los datos iniciales.
    3.  Finalmente, el contenedor de la aplicación Spring Boot (`app`) se iniciará y se conectará a la base de datos ya inicializada.
*   La aplicación estará accesible en `http://localhost:8080`.
*   El servidor SQL estará accesible en `localhost:1433`.

**Importante:** La contraseña del usuario `sa` en `compose.yaml` es `"verYs3cret"`. **Cámbiala por una contraseña segura** en un entorno de producción y en el archivo `compose.yaml`.

Para detener los servicios:

```bash
docker compose down
```

*   Si quieres eliminar también los volúmenes de datos de la base de datos (lo que borrará todos los datos persistidos), usa:
    ```bash
    docker compose down -v
    ```

### 4. Ejecutar la Aplicación (Alternativas sin Docker Compose)

Si prefieres no usar Docker Compose para la aplicación, puedes ejecutarla de las siguientes maneras (requiere una instancia de SQL Server accesible y configurada manualmente, y los scripts SQL ejecutados previamente):

#### 4.1. Configuración Manual de la Base de Datos

Abre el archivo `src/main/resources/application.properties` y actualiza la configuración de la base de datos con tus credenciales y detalles de conexión:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=LoanManagementDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```

Asegúrate de reemplazar `localhost:1433`, `your_username` y `your_password` con los valores correctos para tu entorno.

#### 4.2. Ejecutar Scripts de Inicialización

Los scripts de inicialización de la base de datos se encuentran en la carpeta `src/main/resources/sql-init`. Deberás ejecutar estos scripts en tu instancia de SQL Server para crear la base de datos y las tablas necesarias.

1.  **`01-create-database.sql`**: Crea la base de datos `LoanManagementDB`.
2.  **`02-create-tables.sql`**: Crea todas las tablas del esquema.
3.  **`03-insert-initial-data.sql`**: Inserta datos iniciales para `EstadoSolicitud` y `TipoPlazo`.

#### 4.3. Ejecutar la Aplicación

*   **Opción 1: Desde Maven**
    ```bash
    mvn spring-boot:run
    ```
*   **Opción 2: Ejecutar el archivo JAR**
    Después de `mvn clean install`, encontrarás el archivo `.jar` en el directorio `target/`. Puedes ejecutarlo con:
    ```bash
    java -jar target/loan.management-0.0.1-SNAPSHOT.jar
    ```

La aplicación se iniciará en `http://localhost:8080` por defecto.

## Endpoints de la API

La aplicación expone una API RESTful. La base URL es `http://localhost:8080`.

### 1. Clientes (`/api/clientes`)

*   **`POST /api/clientes`**
    *   **Descripción:** Agrega un nuevo cliente al sistema.
    *   **Request Body (JSON):** `ClienteRequest`
        ```json
        {
          "nombre": "Juan",
          "apellido": "Perez",
          "numeroIdentificacion": "123456789",
          "fechaNacimiento": "1990-01-15",
          "direccion": "Calle Falsa 123",
          "correoElectronico": "juan.perez@example.com",
          "telefono": "5551234"
        }
        ```
    *   **Response (201 Created):** `ClienteResponse`
*   **`GET /api/clientes?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de clientes.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<ClienteResponse>` (objeto Page que contiene la lista de clientes y metadatos de paginación)
*   **`GET /api/clientes/id-cliente/{id}`**
    *   **Descripción:** Obtiene un cliente específico por su ID.
    *   **Path Variable:** `id` (Integer) - ID del cliente.
    *   **Response (200 OK):** `ClienteResponse`
*   **`PUT /api/clientes/id-cliente/{id}`**
    *   **Descripción:** Actualiza la información de un cliente existente.
    *   **Path Variable:** `id` (Integer) - ID del cliente a actualizar.
    *   **Request Body (JSON):** `ClienteRequest` (con los campos a actualizar)
    *   **Response (200 OK):** `ClienteResponse`
*   **`DELETE /api/clientes/id-cliente/{id}`**
    *   **Descripción:** Elimina un cliente del sistema y todas sus solicitudes de préstamos y préstamos asociados.
    *   **Path Variable:** `id` (Integer) - ID del cliente a eliminar.
    *   **Response (204 No Content)**

### 2. Solicitudes de Préstamos (`/api/solicitudes`)

*   **`POST /api/solicitudes`**
    *   **Descripción:** Crea una nueva solicitud de préstamo.
    *   **Request Body (JSON):** `SolicitudPrestamoRequest`
        ```json
        {
          "clienteID": 1,
          "montoSolicitado": 10000.00,
          "plazoID": 1
        }
        ```
    *   **Response (201 Created):** `SolicitudPrestamoResponse`
*   **`GET /api/solicitudes?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de solicitudes de préstamos.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<SolicitudPrestamoResponse>`
*   **`GET /api/solicitudes/cliente-id/{clienteId}?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de solicitudes de préstamo asociadas a un cliente específico.
    *   **Path Variable:** `clienteId` (Integer) - ID del cliente.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<SolicitudPrestamoResponse>`
*   **`GET /api/solicitudes/prestamo-id/{id}`**
    *   **Descripción:** Obtiene una solicitud de préstamo específica por su ID.
    *   **Path Variable:** `id` (Integer) - ID de la solicitud.
    *   **Response (200 OK):** `SolicitudPrestamoResponse`
*   **`PUT /api/solicitudes/prestamo-id/{id}/aprobar`**
    *   **Descripción:** Aprueba una solicitud de préstamo y crea un nuevo préstamo asociado.
    *   **Path Variable:** `id` (Integer) - ID de la solicitud a aprobar.
    *   **Request Body (JSON):**
        ```json
        {
          "detalles": "Aprobado según políticas de crédito."
        }
        ```
    *   **Response (200 OK):** `SolicitudPrestamoResponse` (con estado APROBADO)
*   **`PUT /api/solicitudes/prestamo-id/{id}/rechazar`**
    *   **Descripción:** Rechaza una solicitud de préstamo.
    *   **Path Variable:** `id` (Integer) - ID de la solicitud a rechazar.
    *   **Request Body (JSON):**
        ```json
        {
          "detalles": "Ingresos insuficientes."
        }
        ```
    *   **Response (200 OK):** `SolicitudPrestamoResponse` (con estado RECHAZADO)
*   **`GET /api/solicitudes/tipos-plazo`**
    *   **Descripción:** Obtiene una lista de todos los tipos de plazo disponibles para las solicitudes de préstamo.
    *   **Response (200 OK):** `List<TipoPlazoResponse>`
*   **`GET /api/solicitudes/tipos-plazo/{id}`**
    *   **Descripción:** Obtiene un tipo de plazo específico por su ID.
    *   **Path Variable:** `id` (Integer) - ID del tipo de plazo.
    *   **Response (200 OK):** `TipoPlazoResponse`

### 3. Préstamos (`/api/prestamos`)

*   **`GET /api/prestamos?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de todos los préstamos aprobados en el sistema.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<PrestamoResponse>`
*   **`GET /api/prestamos/cliente-id/{clienteId}?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de préstamos asociados a un cliente específico.
    *   **Path Variable:** `clienteId` (Integer) - ID del cliente.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<PrestamoResponse>`
*   **`GET /api/prestamos/prestamo-id/{id}`**
    *   **Descripción:** Obtiene un préstamo específico por su ID.
    *   **Path Variable:** `id` (Integer) - ID del préstamo.
    *   **Response (200 OK):** `PrestamoResponse`
*   **`GET /api/prestamos/prestamo-id/{id}/saldo-pendiente`**
    *   **Descripción:** Obtiene el saldo pendiente de un préstamo específico.
    *   **Path Variable:** `id` (Integer) - ID del préstamo.
    *   **Response (200 OK):** `BigDecimal` (el monto del saldo pendiente)

### 4. Pagos (`/api/pagos`)

*   **`POST /api/pagos`**
    *   **Descripción:** Registra un nuevo pago para un préstamo existente.
    *   **Request Body (JSON):** `PagoRequest`
        ```json
        {
          "prestamoID": 1,
          "montoPago": 500.00
        }
        ```
    *   **Response (201 Created):** `PagoResponse`
*   **`GET /api/pagos/prestamo/{prestamoId}?page={page}&size={size}`**
    *   **Descripción:** Obtiene una página de todos los pagos realizados para un préstamo específico.
    *   **Path Variable:** `prestamoId` (Integer) - ID del préstamo.
    *   **Parámetros de Query:**
        *   `page` (opcional, default 1): Número de página (1-basado).
        *   `size` (opcional, default 10, max 25): Tamaño de la página.
    *   **Response (200 OK):** `Page<PagoResponse>`

## Ejecución de Pruebas y Reporte de Cobertura

Para ejecutar todas las pruebas unitarias y generar el informe de cobertura de código con JaCoCo:

```bash
mvn clean verify
```

Una vez completado, el informe de cobertura estará disponible en:
`target/site/jacoco/index.html`

Abre este archivo en tu navegador web para ver un desglose detallado de la cobertura de tu código.

## Contribuciones

Las contribuciones son bienvenidas. Por favor, asegúrate de seguir las buenas prácticas de codificación y de incluir pruebas unitarias para cualquier nueva funcionalidad o cambio.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
