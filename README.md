# API de Gestión de Clientes

Proyecto backend desarrollado con **Java y Spring Boot**, que expone un CRUD completo para la gestión de clientes mediante una API REST.

## Tecnologías y herramientas utilizadas
- Java 17
- Spring Boot (Web, Data JPA, Validation, Lombok)
- Hibernate
- MySQL
- Maven
- Docker & Docker Compose
- Swagger/OpenAPI
- Postman

## Funcionalidades principales
- Crear un cliente  
- Listar clientes  
- Consultar cliente por ID  
- Actualizar cliente  
- Eliminar cliente
- Manejo de excepciones
- Documentación automática de endpoints con Swagger  

## Ejecución del proyecto

### 1. Clonar el repositorio
```
git clone https://github.com/wilfredohuarotog/API-RESTful.git
```
### 2. Ingresar al directorio
```
cd clientes
```
### 3. Configuración de variables de entorno del aplication.properties
```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```
### 4. Ejecutar 
```
mvn spring-boot:run
```
### 5. Accede a la documentación
`Documentación`: http://localhost:8080/swagger-ui.html.

## Despliegue en docker
### 1. Generar las imagenes y levantar el servicios del docker-compose.yml
```
docker compose up -build -d
```
### 2. Detener la ejecución
```
docker compose down
```


