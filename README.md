# API de Transferencias Bancarias

## Index

- [descripcion](#descripción)
- [tech stack]
- [paquetes instalados]
- [instalacion]
- [endpoints]
- [estructura]

## Descripción

Esta API permite gestionar transferencias bancarias entre cuentas dentro de un mismo banco y hacia bancos externos.

## Tech Stack

- Java 17

- Spring Boot

- JDBC con JdbcTemplate

- MySQL

- Swagger

## Instalación y ejecución

1. Clonar el repositorio

> git clone https://github.com/FMartine7i/API_banco.git

2. Configurar la base de datos

> Crear application.yml en la carpeta ```/resources``` y luego definir la conexión a la base de datos:

> spring.datasource.url = jdbc:mysql://localhost:3307/banco
spring.datasource.username = root
spring.datasource.password = tu_password
spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
spring.server.port = 8080

3. Ejecutar la aplicación

> mvn spring-boot:run

> [!IMPORTANT]
> Acceder por medio del navegador con la url ``http:\\localhost:8080\swagger-ui\index.html``

## Endpoints

🔹 Obtener historial de movimientos

GET /historial/{nro}

Retorna los movimientos de una cuenta con su origen.

Ejemplo de respuesta

```{
"origen": 123456,
"transacciones": [
    {
    "fecha": "2025-03-06T13:23:35",
    "tipoMovimiento": "TRANSFERENCIA",
    "descripcion": "Pago de servicios",
    "monto": 20000.00
    }
  ]
}
```

🔹 Realizar transferencia

POST /transferir

Permite transferir saldo entre cuentas dentro del mismo banco o hacia bancos externos.

### Estructura
>- ├── src