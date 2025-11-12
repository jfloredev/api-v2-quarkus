# ✅ PROYECTO COMPLETADO - API de Usuarios

## 📦 Lo que se ha creado

### 1. **Entidad Usuario** (`Usuario.java`)
- ID (UUID automático)
- Nombre
- Email
- Métodos para convertir a/desde DynamoDB

### 2. **Servicio** (`UsuarioService.java`)
- ✅ Crear usuario con validación de email único
- ✅ Obtener usuario por ID
- ✅ Listar todos los usuarios
- ✅ Buscar por email (usando índice secundario)
- ✅ Verificar existencia de email antes de crear

### 3. **Controlador REST** (`UsuarioController.java`)
- `POST /api/usuarios` - Crear usuario
- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/{id}` - Obtener por ID
- `GET /api/usuarios/email/{email}` - Buscar por email
- `POST /api/usuarios/init` - Inicializar tabla (solo local)

### 4. **Configuración DynamoDB** (`DynamoDBConfig.java`)
- Productor para inyección de DynamoDbClient
- Soporte para DynamoDB Local y AWS

### 5. **Serverless Framework** (`serverless.yml`)
- ✅ Configurado con org: renadroid
- ✅ Usa IAM Role de AWS Academy
- ✅ Crea tabla DynamoDB automáticamente
- ✅ Nombre de tabla dinámico: `${stage}-usuarios`
- ✅ Índice secundario global para email
- ✅ Modo PAY_PER_REQUEST

### 6. **Scripts**
- `deploy.sh` - Compilar y desplegar a AWS
- `test-api.sh` - Probar API localmente
- `docker-compose.yml` - DynamoDB Local para desarrollo

### 7. **Documentación**
- `README.md` - Documentación principal
- `INICIO_RAPIDO.md` - Guía de inicio rápido
- `GUIA_LOCAL.md` - Desarrollo local detallado
- `DESPLIEGUE.md` - Guía completa de despliegue
- `API_USUARIOS.md` - Documentación de endpoints
- `DYNAMODB_EXPLICACION.md` - Por qué usa "tablas"

## 🎯 Funcionalidades implementadas

### ✅ Validación de email único
Cuando intentas crear un usuario con un email existente:
```json
{
  "error": "Ya existe un usuario con este email: juan@example.com"
}
```

### ✅ Campos requeridos
- Nombre no puede estar vacío
- Email no puede estar vacío

### ✅ Búsqueda eficiente por email
Usa un Global Secondary Index en DynamoDB para búsquedas rápidas.

## 🚀 Cómo usar

### En AWS (Producción)
```bash
git clone <repo>
cd api-v2-quarkus
chmod +x deploy.sh
./deploy.sh dev
```

### Local (Desarrollo)
```bash
docker-compose up -d
./mvnw quarkus:dev
curl -X POST http://localhost:8080/api/usuarios/init
./test-api.sh
```

## 📊 Estructura DynamoDB

**Tabla:** `${stage}-usuarios` (ej: `dev-usuarios`, `prod-usuarios`)

**Clave primaria:**
- `id` (HASH) - UUID generado automáticamente

**Índice secundario global:**
- `email-index` - Para búsquedas por email y validación de unicidad

**Atributos:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

## 🔧 Configuración importante

### Variables de entorno
- `TABLE_NAME` - Se configura automáticamente por Serverless (`${stage}-usuarios`)
- `DYNAMODB_ENDPOINT` - Solo para desarrollo local (`http://localhost:8000`)

### application.properties
Para desarrollo local, descomentar:
```properties
quarkus.dynamodb.endpoint-override=http://localhost:8000
```

Para producción, dejar comentado (usa AWS real).

## 📝 Notas técnicas

1. **¿Por qué usa "tabla" si es NoSQL?**
   - DynamoDB usa el término "tabla" como contenedor lógico
   - No tiene esquema rígido como SQL
   - Ver `DYNAMODB_EXPLICACION.md` para más detalles

2. **¿Por qué PAY_PER_REQUEST?**
   - Solo pagas por las requests que hagas
   - No necesitas provisionar capacidad
   - Ideal para desarrollo y APIs con tráfico variable

3. **¿Cómo funciona la validación de email único?**
   - Antes de crear, se hace un Query al índice `email-index`
   - Si retorna resultados (count > 0), rechaza la creación
   - Usa el índice secundario para ser eficiente

## ✅ Todo listo para

- ✅ Subir a Git
- ✅ Clonar en máquina virtual
- ✅ Desplegar con `./deploy.sh dev`
- ✅ Probar la API
- ✅ Validar que no se puedan crear emails duplicados

## 🎉 Resultado final

Un API REST completamente funcional que:
- Maneja usuarios con id, nombre y email
- Valida que no se repitan emails
- Se despliega en AWS Lambda
- Usa DynamoDB como base de datos
- Incluye documentación completa
- Tiene scripts para facilitar desarrollo y despliegue

