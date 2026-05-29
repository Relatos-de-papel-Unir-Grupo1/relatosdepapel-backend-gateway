# Relatos de Papel - API Gateway

El API Gateway es el único punto de entrada para todas las peticiones de los clientes (como una aplicación web o móvil) hacia el ecosistema de microservicios de **Relatos de Papel**. Su función principal es enrutar las peticiones entrantes al servicio correspondiente.

## ✨ Características Principales

- **Enrutamiento Dinámico**: Utiliza Spring Cloud Gateway para enrutar las peticiones a los microservicios adecuados basándose en el path de la URL.
- **Integración con Eureka**: Descubre automáticamente las ubicaciones de los microservicios (Catálogo, Órdenes, etc.) a través de Eureka, eliminando la necesidad de configurar URLs estáticas.
- **Balanceo de Carga**: Al obtener las instancias de servicio de Eureka, distribuye la carga entre las instancias disponibles de un mismo microservicio.
- **Fachada del Sistema**: Abstrae la complejidad de la arquitectura de microservicios de cara al cliente, proporcionando una única API consolidada.
- **Punto Central para Lógicas Transversales**: Es el lugar ideal para implementar lógicas como autenticación, autorización, logging, rate limiting, etc.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 17
- **Framework**: Spring Boot 3
- **Gateway**: Spring Cloud Gateway
- **Service Discovery**: Spring Cloud Netflix Eureka Client
- **Gestión de Dependencias**: Maven

## 🚀 Cómo Empezar

Sigue estos pasos para configurar y ejecutar el API Gateway.

### Prerrequisitos

- JDK 17 o superior.
- Maven 3.8 o superior.
- Un servidor Eureka en ejecución.
- Los microservicios de backend (Catálogo, Órdenes) deben estar en ejecución y registrados en Eureka.

### Configuración

1.  **Clona el repositorio**:
    ```bash
    git clone <url-del-repositorio-gateway>
    cd relatosdepapel-api-gateway
    ```

2.  **Configura las propiedades de la aplicación**:
    El archivo `src/main/resources/application.yml` (o `.properties`) define las rutas. La configuración se basa en los nombres de los servicios registrados en Eureka.

    ```yaml
    server:
      port: 8080 # Puerto de entrada para los clientes

    # Configuración de Eureka
    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8761/eureka

    # Configuración de Spring Cloud Gateway
    spring:
      cloud:
        gateway:
          discovery:
            locator:
              enabled: true # Habilita el enrutamiento basado en Eureka
          routes:
            # Ruta para el servicio de catálogo
            - id: catalogue-service
              uri: lb://CATALOGUE # "lb" indica balanceo de carga, "CATALOGUE" es el nombre del servicio en Eureka
              predicates:
                - Path=/api/v1/books/**
            
            # Ruta para el servicio de órdenes
            - id: orders-service
              uri: lb://ORDERS # "ORDERS" es el nombre del servicio en Eureka
              predicates:
                - Path=/api/v1/orders/**
    ```

### Ejecución

Puedes ejecutar la aplicación utilizando el siguiente comando de Maven:

```bash
mvn spring-boot:run
```

El API Gateway estará escuchando en `http://localhost:8080`. Todas las peticiones a los microservicios deben pasar a través de esta URL.

**Ejemplos de Peticiones a través del Gateway:**

- Para acceder al listado de libros del catálogo: `GET http://localhost:8080/api/v1/books`
- Para crear una orden: `POST http://localhost:8080/api/v1/orders`

## 👥 Colaboradores

- **Sebastian Felipe Alvarado Prieto**
- **Ardys Díaz Hurtado**
- **Luis Ferdinand Lugoz Rivas**

```

Ahora sí, la documentación de la infraestructura principal está completa. ¡Gracias por señalarlo!

<!--
[PROMPT_SUGGESTION]Ahora, ayúdame a crear un archivo `docker-compose.yml` global para levantar todo el entorno (Eureka, Gateway, Catálogo, Órdenes y MySQL).[/PROMPT_SUGGESTION]
[PROMPT_SUGGESTION]¿Cómo puedo añadir seguridad con Spring Security y JWT en el API Gateway para proteger los endpoints?[/PROMPT_SUGGESTION]
