# 🚀 INICIO RÁPIDO

## Para desplegar en AWS (desde cero)

```bash
# 1. Clonar
git clone <tu-repo>
cd api-v2-quarkus

# 2. Dar permisos
chmod +x deploy.sh

# 3. Compilar y desplegar
./deploy.sh dev

# 4. Probar (usar el endpoint que te da el comando anterior)
export API_URL=https://xxxxxx.execute-api.us-east-1.amazonaws.com/dev

curl -X POST $API_URL/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test User","email":"test@test.com"}'

curl $API_URL/api/usuarios
```

## ✅ Listo!

La tabla `dev-usuarios` se crea automáticamente con:
- Clave primaria: `id` (UUID)
- Índice secundario: `email` (para validar únicos)
- Modo: PAY_PER_REQUEST (solo pagas lo que usas)

## 🔄 Actualizar despliegue

```bash
./mvnw clean package -DskipTests
serverless deploy --stage dev
```

## 🗑️ Eliminar todo

```bash
serverless remove --stage dev
```

## 📱 Prueba de email duplicado

```bash
# Crear primer usuario
curl -X POST $API_URL/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","email":"juan@test.com"}'

# Intentar crear con mismo email (debe fallar)
curl -X POST $API_URL/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","email":"juan@test.com"}'

# Respuesta: {"error":"Ya existe un usuario con este email: juan@test.com"}
```

