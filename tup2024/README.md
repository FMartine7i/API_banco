# API de Transferencias Bancarias

### Descripción

Esta API permite gestionar transferencias bancarias entre cuentas dentro de un mismo banco y hacia bancos externos.

### Tecnologías utilizadas

Java 17

Spring Boot

JDBC con JdbcTemplate

MySQL

### Instalación y ejecución

1️⃣ Clonar el repositorio

git clone https://github.com/FMartine7i/API_banco.git
cd tup2024

2️⃣ Configurar la base de datos

Modificar application.yml para definir la conexión a la base de datos:

spring.datasource.url=jdbc:mysql://localhost:3307/banco
spring.datasource.username=root
spring.datasource.password=tu_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

3️⃣ Ejecutar la aplicación

mvn spring-boot:run

### Endpoints

🔹 Obtener historial de movimientos

GET /historial/{nro}

Retorna los movimientos de una cuenta con su origen.

Ejemplo de respuesta

{
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

🔹 Realizar transferencia

POST /transferir

Permite transferir saldo entre cuentas dentro del mismo banco o hacia bancos externos.

### Estructura
