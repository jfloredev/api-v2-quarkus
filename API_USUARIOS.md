# API de Usuarios

API REST simple para gestionar usuarios con validación de emails únicos.

## Entidad Usuario

La entidad `Usuario` tiene los siguientes campos:
- **id**: String (generado automáticamente con UUID)
- **nombre**: String (requerido)
- **email**: String (requerido, único)

## Endpoints disponibles

### 1. Crear Usuario
```
POST /api/usuarios
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

**Respuesta exitosa (201):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

**Respuesta error (400):**
```json
{
  "error": "Ya existe un usuario con este email: juan@example.com"
}
```

### 2. Obtener todos los usuarios
```
GET /api/usuarios
```

**Respuesta (200):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "nombre": "María García",
    "email": "maria@example.com"
  }
]
```

### 3. Obtener usuario por ID
```
GET /api/usuarios/{id}
```

**Respuesta (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

**Respuesta error (404):**
```json
{
  "error": "Usuario no encontrado"
}
```

### 4. Buscar usuario por email
```
GET /api/usuarios/email/{email}
```

### 5. Inicializar tabla DynamoDB (solo desarrollo)
```
POST /api/usuarios/init
```

Este endpoint crea la tabla en DynamoDB si no existe.

## Validaciones implementadas

1. **Email único**: No se puede registrar el mismo email dos veces
2. **Campos requeridos**: Nombre y email son obligatorios
3. **Campos no vacíos**: No se aceptan valores vacíos o solo espacios

## Configuración

### DynamoDB Local (para desarrollo)
Para usar DynamoDB Local, descomentar en `application.properties`:
```properties
quarkus.dynamodb.endpoint-override=http://localhost:8000
```

### AWS DynamoDB (producción)
Configurar las variables de entorno:
```bash
export AWS_ACCESS_KEY_ID=tu_access_key
export AWS_SECRET_ACCESS_KEY=tu_secret_key
export AWS_REGION=us-east-1
```

## Ejecutar la aplicación

### Modo desarrollo
```bash
./mvnw quarkus:dev
```

### Compilar y ejecutar
```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## Ejemplos de uso con curl

### Crear un usuario
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@example.com"}'
```

### Listar todos los usuarios
```bash
curl http://localhost:8080/api/usuarios
```

### Obtener un usuario por ID
```bash
curl http://localhost:8080/api/usuarios/550e8400-e29b-41d4-a716-446655440000
```

### Buscar por email
```bash
curl http://localhost:8080/api/usuarios/email/juan@example.com
```

### Inicializar tabla
```bash
curl -X POST http://localhost:8080/api/usuarios/init
```

## Estructura del proyecto

```
src/main/java/com/jfloresdev/
├── config/
│   └── DynamoDBConfig.java          # Configuración de DynamoDB
└── user/
    ├── controller/
    │   └── UsuarioController.java   # Endpoints REST
    ├── entity/
    │   └── Usuario.java             # Entidad Usuario
    └── service/
        └── UsuarioService.java      # Lógica de negocio
```

