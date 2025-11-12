# 📦 Guía de Despliegue a AWS

Esta guía te ayudará a desplegar la API de usuarios en AWS Lambda usando Serverless Framework.

## 📋 Prerequisitos

1. **AWS CLI configurado**
2. **Node.js y npm** instalados
3. **Serverless Framework** instalado
4. **Credenciales AWS** con permisos para:
   - Lambda
   - API Gateway
   - DynamoDB
   - CloudFormation
   - IAM

## 🚀 Pasos para Desplegar

### 1. Instalar Serverless Framework (si no lo tienes)

```bash
npm install -g serverless
```

### 2. Configurar credenciales AWS

```bash
# Opción A: Configurar con AWS CLI
aws configure

# Opción B: Variables de entorno
export AWS_ACCESS_KEY_ID=tu_access_key_id
export AWS_SECRET_ACCESS_KEY=tu_secret_access_key
export AWS_REGION=us-east-1
```

### 3. Clonar y preparar el proyecto

```bash
# Clonar el repositorio
git clone <tu-repositorio>
cd api-v2-quarkus

# Dar permisos de ejecución a los scripts
chmod +x deploy.sh test-api.sh
```

### 4. Instalar dependencias de Serverless

```bash
npm install --save-dev serverless-offline
```

### 5. Compilar el proyecto

```bash
# Compilar para Lambda (JVM)
./mvnw clean package

# O compilar nativo (más rápido en ejecución, más lento en compilación)
./mvnw clean package -Pnative -DskipTests
```

### 6. Desplegar a AWS

#### Opción A: Usando el script automático

```bash
# Desplegar a dev
./deploy.sh dev

# Desplegar a producción
./deploy.sh prod
```

#### Opción B: Manualmente

```bash
# Desplegar
serverless deploy --stage dev --verbose

# Ver logs
serverless logs -f api --stage dev --tail

# Eliminar despliegue
serverless remove --stage dev
```

## 📝 Después del Despliegue

### 1. Obtener la URL del API

Después del despliegue verás algo como:

```
endpoints:
  ANY - https://abc123xyz.execute-api.us-east-1.amazonaws.com/dev/{proxy+}
  ANY - https://abc123xyz.execute-api.us-east-1.amazonaws.com/dev
```

### 2. Inicializar la tabla DynamoDB

```bash
# Reemplaza con tu URL
export API_URL=https://abc123xyz.execute-api.us-east-1.amazonaws.com/dev

curl -X POST $API_URL/api/usuarios/init
```

### 3. Probar la API

```bash
# Crear usuario
curl -X POST $API_URL/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }'

# Listar usuarios
curl $API_URL/api/usuarios

# Buscar por email
curl $API_URL/api/usuarios/email/juan@example.com
```

## 🔧 Configuración Avanzada

### Cambiar región AWS

Edita `serverless.yml`:

```yaml
provider:
  region: us-west-2  # Cambia a tu región preferida
```

### Ajustar memoria y timeout

```yaml
provider:
  memorySize: 1024  # MB
  timeout: 60       # segundos
```

### Configurar dominio personalizado

Instala el plugin:

```bash
npm install --save-dev serverless-domain-manager
```

Agrega a `serverless.yml`:

```yaml
plugins:
  - serverless-domain-manager

custom:
  customDomain:
    domainName: api.tudominio.com
    stage: prod
    certificateName: '*.tudominio.com'
    createRoute53Record: true
```

## 📊 Monitoreo

### Ver logs en tiempo real

```bash
serverless logs -f api --tail --stage dev
```

### Ver métricas en AWS Console

1. Ve a [AWS Lambda Console](https://console.aws.amazon.com/lambda)
2. Busca la función: `api-usuarios-quarkus-dev-api`
3. Ve a la pestaña "Monitoring"

### Ver tabla DynamoDB

1. Ve a [DynamoDB Console](https://console.aws.amazon.com/dynamodb)
2. Busca la tabla: `usuarios`
3. Explora los items

## 💰 Costos Estimados

Con **AWS Free Tier**:
- **Lambda**: 1M invocaciones gratis/mes
- **DynamoDB**: 25 GB almacenamiento gratis
- **API Gateway**: 1M llamadas gratis/mes

Después del Free Tier (uso moderado):
- ~$0.20 USD/mes por Lambda
- ~$1-5 USD/mes por DynamoDB
- ~$3.50 USD/mes por API Gateway

**Total estimado**: $5-10 USD/mes para uso moderado

## 🔄 Actualizar el Despliegue

```bash
# Hacer cambios en el código
# ...

# Compilar y redesplegar
./mvnw clean package
serverless deploy --stage dev
```

## 🗑️ Eliminar Todo

```bash
# Eliminar el stack completo
serverless remove --stage dev

# Esto eliminará:
# - Función Lambda
# - API Gateway
# - Tabla DynamoDB (¡cuidado con los datos!)
# - Roles IAM
```

## 🐛 Troubleshooting

### Error: "Insufficient permissions"

Asegúrate de que tu usuario IAM tenga los permisos necesarios.

### Error: "Table already exists"

Si la tabla ya existe, comenta el recurso en `serverless.yml`:

```yaml
resources:
  Resources:
    # UsuariosTable:
    #   Type: AWS::DynamoDB::Table
    #   ...
```

### Error: "Cannot find function.zip"

Asegúrate de compilar primero:

```bash
./mvnw clean package
```

### Logs no aparecen

Espera unos segundos después del despliegue:

```bash
sleep 10
serverless logs -f api --tail --stage dev
```

## 📚 Comandos Útiles

```bash
# Ver información del despliegue
serverless info --stage dev

# Invocar función directamente
serverless invoke -f api --stage dev

# Ver métricas
serverless metrics --stage dev

# Desplegar solo función (más rápido)
serverless deploy function -f api --stage dev
```

## 🔐 Seguridad

### Agregar autenticación (API Key)

En `serverless.yml`:

```yaml
functions:
  api:
    events:
      - http:
          path: /{proxy+}
          method: ANY
          private: true  # Requiere API Key

provider:
  apiGateway:
    apiKeys:
      - mi-api-key
```

### Agregar CORS personalizado

```yaml
functions:
  api:
    events:
      - http:
          cors:
            origin: 'https://tuapp.com'
            headers:
              - Content-Type
              - Authorization
            allowCredentials: true
```

## ✅ Checklist de Despliegue

- [ ] AWS CLI configurado
- [ ] Credenciales AWS válidas
- [ ] Proyecto compilado exitosamente
- [ ] Serverless Framework instalado
- [ ] Archivo serverless.yml revisado
- [ ] Despliegue exitoso
- [ ] Tabla DynamoDB inicializada
- [ ] Endpoint probado con curl
- [ ] Validación de email duplicado funcionando
- [ ] Logs revisados para errores

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs: `serverless logs -f api --tail --stage dev`
2. Verifica la tabla DynamoDB en AWS Console
3. Revisa CloudWatch Logs
4. Verifica los permisos IAM

