# 🚀 Guía de Inicio Rápido - Desarrollo Local

Esta guía te ayudará a probar la API de usuarios en tu máquina local.

## Prerequisitos

- Java 17 o superior
- Maven 3.8+
- Docker y Docker Compose (para DynamoDB Local)

## Paso 1: Levantar DynamoDB Local

### Opción A: Usando Docker Compose (Recomendado)

```bash
# Iniciar DynamoDB Local
docker-compose up -d

# Verificar que esté corriendo
docker ps
```

Deberías ver algo como:
```
CONTAINER ID   IMAGE                      STATUS         PORTS
abc123def456   amazon/dynamodb-local     Up 5 seconds   0.0.0.0:8000->8000/tcp
```

### Opción B: Sin Docker (Descarga directa)

Si no tienes Docker, descarga DynamoDB Local:
```bash
# Descargar
curl -O https://s3-us-west-2.amazonaws.com/dynamodb-local/dynamodb_local_latest.tar.gz
tar -xzf dynamodb_local_latest.tar.gz

# Ejecutar
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -inMemory
```

## Paso 2: Configurar la aplicación para desarrollo local

Edita el archivo `src/main/resources/application.properties` y descomenta la línea:

```properties
quarkus.dynamodb.endpoint-override=http://localhost:8000
```

O usa la variable de entorno:
```bash
export DYNAMODB_ENDPOINT=http://localhost:8000
```

## Paso 3: Iniciar la aplicación Quarkus

```bash
# En modo desarrollo (con hot reload)
./mvnw quarkus:dev
```

La aplicación estará disponible en: **http://localhost:8080**

## Paso 4: Inicializar la tabla de usuarios

Primero, crea la tabla en DynamoDB Local:

```bash
curl -X POST http://localhost:8080/api/usuarios/init
```

Respuesta esperada:
```json
{
  "mensaje": "Tabla inicializada correctamente"
}
```

## Paso 5: Probar la API

### 1️⃣ Crear un usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }'
```

### 2️⃣ Intentar crear usuario con email duplicado (debe fallar)

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Pedro López",
    "email": "juan@example.com"
  }'
```

Respuesta esperada:
```json
{
  "error": "Ya existe un usuario con este email: juan@example.com"
}
```

### 3️⃣ Crear otro usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María García",
    "email": "maria@example.com"
  }'
```

### 4️⃣ Listar todos los usuarios

```bash
curl http://localhost:8080/api/usuarios
```

### 5️⃣ Buscar usuario por email

```bash
curl http://localhost:8080/api/usuarios/email/juan@example.com
```

### 6️⃣ Obtener usuario por ID

```bash
# Usa el ID que obtuviste al crear el usuario
curl http://localhost:8080/api/usuarios/550e8400-e29b-41d4-a716-446655440000
```

## 📊 Verificar datos en DynamoDB Local

### Usando AWS CLI Local

```bash
# Instalar AWS CLI si no lo tienes
# brew install awscli  # macOS
# pip install awscli   # pip

# Configurar credenciales dummy (para local)
aws configure set aws_access_key_id "dummy"
aws configure set aws_secret_access_key "dummy"
aws configure set region "us-east-1"

# Listar tablas
aws dynamodb list-tables --endpoint-url http://localhost:8000

# Ver contenido de la tabla
aws dynamodb scan --table-name usuarios --endpoint-url http://localhost:8000
```

### Usando GUI - NoSQL Workbench

Descarga [NoSQL Workbench](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html) para una interfaz visual.

## 🛠️ Script de prueba completo

Crea un archivo `test-api.sh`:

```bash
#!/bin/bash

echo "🚀 Iniciando pruebas de la API de Usuarios"
echo ""

echo "1️⃣ Inicializando tabla..."
curl -X POST http://localhost:8080/api/usuarios/init
echo -e "\n"

echo "2️⃣ Creando primer usuario (Juan)..."
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@example.com"}'
echo -e "\n"

echo "3️⃣ Creando segundo usuario (María)..."
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María García","email":"maria@example.com"}'
echo -e "\n"

echo "4️⃣ Intentando crear usuario duplicado (debe fallar)..."
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro López","email":"juan@example.com"}'
echo -e "\n"

echo "5️⃣ Listando todos los usuarios..."
curl http://localhost:8080/api/usuarios
echo -e "\n"

echo "6️⃣ Buscando por email..."
curl http://localhost:8080/api/usuarios/email/maria@example.com
echo -e "\n"

echo "✅ Pruebas completadas!"
```

Dale permisos y ejecútalo:
```bash
chmod +x test-api.sh
./test-api.sh
```

## 🐛 Troubleshooting

### Error: "Unsatisfied dependency DynamoDbClient"

Asegúrate de que el archivo `DynamoDBConfig.java` exista en `src/main/java/com/jfloresdev/config/`

### Error: "Cannot connect to DynamoDB"

Verifica que DynamoDB Local esté corriendo:
```bash
docker ps
# o
curl http://localhost:8000
```

### Error: "ResourceNotFoundException"

Inicializa la tabla:
```bash
curl -X POST http://localhost:8080/api/usuarios/init
```

## 🧹 Limpiar

```bash
# Detener DynamoDB Local
docker-compose down

# Detener Quarkus (Ctrl+C en la terminal donde está corriendo)

# Limpiar build
./mvnw clean
```

## 📝 Próximos pasos

Una vez que todo funcione localmente:

1. ✅ La validación de email único está funcionando
2. ✅ Los usuarios se guardan correctamente
3. ✅ Puedes buscar y listar usuarios

Para producción:
- Quita la línea `quarkus.dynamodb.endpoint-override` de `application.properties`
- Configura las credenciales AWS reales
- Despliega en AWS Lambda o ECS

