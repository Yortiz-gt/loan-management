# Guía de Desarrollo y Buenas Prácticas

Este documento complementa el `README.md` proporcionando una visión más profunda sobre la arquitectura, las decisiones de diseño y las buenas prácticas implementadas en el Sistema de Gestión de Préstamos. Está dirigido a desarrolladores que deseen entender, mantener o extender el proyecto.

## 1. Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas, común en aplicaciones Spring Boot, con un enfoque en la separación de responsabilidades:

*   **`web` (Capa de Presentación/Controladores):** Contiene los `RestController` que exponen la API REST. Su única responsabilidad es recibir las peticiones HTTP, validar los DTOs de entrada, delegar la lógica de negocio a la capa de servicio y devolver las respuestas HTTP adecuadas. Son "delgados" y no contienen lógica de negocio ni de mapeo de entidades.
*   **`svc` (Capa de Servicio/Lógica de Negocio):** Contiene las interfaces de servicio y sus implementaciones (`ServiceImpl`). Aquí reside toda la lógica de negocio de la aplicación. Orquestan las operaciones, aplican validaciones de negocio, manejan transacciones y utilizan los repositorios para interactuar con la base de datos. Trabajan con DTOs de entrada y devuelven DTOs de salida.
*   **`dao` (Capa de Acceso a Datos):** Contiene las interfaces de repositorio que extienden `JpaRepository` de Spring Data JPA. Son responsables de la interacción directa con la base de datos (CRUD). Las consultas personalizadas se definen aquí, preferiblemente usando `JOIN FETCH` para optimizar la carga de datos y evitar problemas N+1.
*   **`model` (Capa de Dominio/Entidades JPA):** Define las entidades que mapean a las tablas de la base de datos. Contienen la estructura de los datos y, ocasionalmente, lógica de dominio intrínseca (ej. métodos `onCreate`/`onUpdate` con `@PrePersist`/`@PreUpdate`).
*   **`dto` (Data Transfer Objects):** Clases simples utilizadas para transferir datos entre las capas (principalmente entre `web` y `svc`). Separan el contrato de la API del modelo de dominio interno, protegiendo la API de cambios en el modelo.
*   **`mapper` (Capa de Mapeo):** Clases dedicadas a la conversión entre entidades de `model` y DTOs. Centralizan esta lógica, evitando la duplicación y manteniendo los controladores y servicios limpios.
*   **`exception` (Manejo de Excepciones):** Clases de excepción personalizadas para manejar errores específicos de la aplicación de manera controlada y semántica, a menudo con anotaciones `@ResponseStatus` para mapear a códigos de estado HTTP.

## 2. Principios de Diseño y Buenas Prácticas

Durante el desarrollo y la refactorización, se han aplicado los siguientes principios:

*   **Single Responsibility Principle (SRP):** Cada clase y método tiene una única razón para cambiar. Los controladores manejan HTTP, los servicios la lógica de negocio, los repositorios la persistencia, y los mappers la conversión.
*   **Don't Repeat Yourself (DRY):** Se ha evitado la duplicación de código. Ejemplos incluyen la centralización de la lógica de búsqueda de entidades en métodos privados dentro de los servicios y el uso de mappers.
*   **Uso de DTOs:** La API expone DTOs en lugar de entidades de dominio, lo que proporciona un contrato estable y desacopla las capas.
*   **Mappers Dedicados:** La lógica de conversión entre DTOs y entidades se encuentra en clases `Mapper` separadas, lo que facilita su mantenimiento y reutilización.
*   **Manejo de Excepciones Controlado:** Se utilizan excepciones personalizadas con `@ResponseStatus` para proporcionar respuestas de error claras y significativas a los clientes de la API.
*   **Transaccionalidad (`@Transactional`):** Las operaciones que modifican el estado de la base de datos están anotadas con `@Transactional` para asegurar la atomicidad y consistencia de los datos.
*   **Seguridad contra Inyección SQL:** El uso de Spring Data JPA y consultas parametrizadas (ya sea por nombre de método o con `@Query` y `@Param`) protege automáticamente contra la inyección SQL.
*   **Lombok para Reducir Boilerplate:** Se utiliza Lombok para generar automáticamente getters, setters, constructores, etc., manteniendo el código más conciso. Se ha tenido cuidado con `@Data` en entidades JPA, reemplazándolo por `@Getter`, `@Setter`, `@ToString(exclude = ...)` y `@EqualsAndHashCode(exclude = ...)` para evitar problemas con la carga perezosa y las relaciones.
*   **API de Fecha y Hora Moderna:** Se utiliza `java.time` (LocalDate, LocalDateTime) de forma consistente para el manejo de fechas y horas, que es más robusta e inmutable que `java.util.Date`.
*   **Contenerización con Docker Compose:** Se utiliza Docker Compose para definir y ejecutar la aplicación y sus servicios dependientes (como la base de datos SQL Server) en contenedores. Esto asegura un entorno de desarrollo consistente y fácil de configurar para todos los desarrolladores.
*   **Paginación Configurable:** La API implementa paginación para listados, con valores por defecto y máximos configurables a través de `application.properties`. La paginación es 1-basada para el usuario y se convierte a 0-basada internamente para Spring Data JPA.

## 3. Cómo Extender el Proyecto

Si necesitas añadir nuevas funcionalidades o entidades:

1.  **Nueva Entidad:**
    *   Crea la clase en `com.bank.loan.management.model`.
    *   Define su `Repository` en `com.bank.loan.management.dao`.
    *   Crea los DTOs (`Request` y `Response`) en `com.bank.loan.management.dto`.
    *   Crea un `Mapper` en `com.bank.loan.management.mapper`.
    *   Implementa la lógica de negocio en un nuevo `Service` (interfaz y `ServiceImpl`) en `com.bank.loan.management.svc`.
    *   Expón la funcionalidad a través de un `Controller` en `com.bank.loan.management.web`.
    *   **¡No olvides escribir pruebas unitarias!**

2.  **Nueva Lógica de Negocio:**
    *   Identifica el servicio (`ServiceImpl`) adecuado para la nueva lógica.
    *   Añade el nuevo método a la interfaz del servicio.
    *   Implementa la lógica en el `ServiceImpl`, utilizando los repositorios y mappers necesarios.
    *   Si es una operación de escritura, asegúrate de que el método sea `@Transactional`.
    *   **¡Escribe pruebas unitarias para la nueva lógica!**

## 4. Ejecución de Pruebas y Cobertura de Código

Es fundamental ejecutar las pruebas regularmente y revisar el informe de cobertura.

*   **Ejecutar Pruebas y Generar Reporte:**
    ```bash
    mvn clean verify
    ```
*   **Ver el Reporte:** Abre `target/site/jacoco/index.html` en tu navegador.
*   **Interpretación del Reporte:**
    *   **Cobertura de Líneas (Line Coverage):** Indica el porcentaje de líneas de código que fueron ejecutadas por las pruebas.
    *   **Cobertura de Ramas (Branch Coverage):** Indica el porcentaje de ramas condicionales (ej. `if/else`, `switch`) que fueron ejecutadas.
    *   **Colores en el Código Fuente:**
        *   **Verde:** Líneas/ramas completamente cubiertas.
        *   **Amarillo:** Líneas/ramas parcialmente cubiertas (ej. un `if` se probó, pero el `else` no).
        *   **Rojo:** Líneas/ramas no cubiertas en absoluto.

    **Objetivo:** Esforzarse por tener la mayor cantidad de código en verde posible, prestando especial atención a las líneas en amarillo y rojo en la lógica de negocio crítica.

## 5. Solución de Problemas Comunes

*   **`ClienteNotFoundException` / `PrestamoNotFoundException` / `SolicitudNotFoundException` / `ResourceNotFoundException`:** Asegúrate de que el ID proporcionado en la URL o en el cuerpo de la petición corresponde a un recurso existente en la base de datos.
*   **`ClienteAlreadyExistsException`:** Estás intentando crear un cliente con un `numeroIdentificacion` o `correoElectronico` que ya está registrado.
*   **`InvalidSolicitudStatusException`:** Estás intentando aprobar o rechazar una solicitud que no se encuentra en estado "EN_PROCESO".
*   **Problemas de Conexión a la Base de Datos:** Verifica la configuración en `application.properties` (URL, usuario, contraseña) y asegúrate de que tu instancia de SQL Server esté en ejecución y accesible. Si usas Docker Compose, verifica que los contenedores estén levantados y saludables (`docker compose ps`).
*   **Fallo en la Construcción por Cobertura (si se habilita):** Si re-habilitas la regla de `minimum` en JaCoCo y la construcción falla, significa que la cobertura de tus pruebas está por debajo del umbral configurado. Revisa el informe de JaCoCo para identificar qué partes del código necesitan más pruebas.

Esta guía debería ser un buen punto de partida para cualquier desarrollador que se una al proyecto.
