# API de Usuarios - Quarkus + DynamoDB

API REST para gestionar usuarios con validación de emails únicos, construida con Quarkus y DynamoDB.

## 📋 Características

- ✅ CRUD de usuarios (Crear, Leer, Listar)
- ✅ Validación de email único
- ✅ Almacenamiento en DynamoDB
- ✅ Búsqueda por email usando índice secundario
- ✅ Despliegue en AWS Lambda con Serverless Framework

## 🚀 Despliegue a AWS (Producción)

### 1. Clonar el proyecto

```bash
git clone <tu-repositorio>
cd api-v2-quarkus
chmod +x deploy.sh
```

### 2. Compilar y desplegar

```bash
./deploy.sh dev
```

Esto hará:
- Compilar el proyecto con Maven
- Crear la tabla DynamoDB `dev-usuarios`
- Desplegar la función Lambda
- Crear el API Gateway

### 3. Probar el API

```bash
# Usar el endpoint que te da el deploy
export API_URL=https://xxxxxx.execute-api.us-east-1.amazonaws.com/dev

# Crear usuario
curl -X POST $API_URL/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@test.com"}'

# Listar usuarios
curl $API_URL/api/usuarios
```

## 💻 Desarrollo Local

### 1. Levantar DynamoDB Local

```bash
docker-compose up -d
```

### 2. Configurar para local

Edita `src/main/resources/application.properties` y descomenta:

```properties
quarkus.dynamodb.endpoint-override=http://localhost:8000
```

### 3. Iniciar Quarkus en modo dev

```bash
./mvnw quarkus:dev
```

### 4. Inicializar tabla y probar

```bash
# Inicializar tabla
curl -X POST http://localhost:8080/api/usuarios/init

# Probar endpoints
./test-api.sh
```

## 📚 Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/usuarios` | Crear usuario |
| GET | `/api/usuarios` | Listar todos |
| GET | `/api/usuarios/{id}` | Obtener por ID |
| GET | `/api/usuarios/email/{email}` | Buscar por email |
| POST | `/api/usuarios/init` | Inicializar tabla (solo local) |

## 🗂️ Estructura del Proyecto

```
src/main/java/com/jfloresdev/
├── config/
│   └── DynamoDBConfig.java          # Configuración DynamoDB
└── user/
    ├── controller/
    │   └── UsuarioController.java   # REST endpoints
    ├── entity/
    │   └── Usuario.java             # Entidad Usuario
    └── service/
        └── UsuarioService.java      # Lógica de negocio
```

## 📖 Documentación Adicional

- [GUIA_LOCAL.md](GUIA_LOCAL.md) - Desarrollo local detallado
- [DESPLIEGUE.md](DESPLIEGUE.md) - Guía completa de despliegue
- [API_USUARIOS.md](API_USUARIOS.md) - Documentación de la API
- [DYNAMODB_EXPLICACION.md](DYNAMODB_EXPLICACION.md) - Por qué DynamoDB usa "tablas"

## 🛠️ Tecnologías

- **Quarkus 3.29.2** - Framework Java
- **DynamoDB** - Base de datos NoSQL
- **AWS Lambda** - Serverless compute
- **Serverless Framework** - Deployment tool

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/api-v2-quarkus-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- AWS Lambda Gateway REST API ([guide](https://quarkus.io/guides/aws-lambda-http)): Build an API Gateway REST API with
  Lambda integration
- Logging JSON ([guide](https://quarkus.io/guides/logging#json-logging)): Add JSON formatter for console logging
- Amazon DynamoDB ([guide](https://docs.quarkiverse.io/quarkus-amazon-services/dev/amazon-dynamodb.html)): Connect to
  Amazon DynamoDB datastore

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
