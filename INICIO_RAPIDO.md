# 🚀 INICIO RÁPIDO

## Para desplegar en AWS (después de configurar la VM)

Si es la primera vez que usas la VM, ejecuta esto primero:
# Si ya clonaste y configuraste, solo necesitas:
git clone <tu-repo>
# 1. Dar permisos (si no lo hiciste)

# 2. Configurar el entorno (instala Java, Node, Serverless, etc)
# 2. Compilar y desplegar
./setup-vm.sh
# 2. Compilar y desplegar
# 3. Reiniciar sesión para aplicar configuración
source ~/.bashrc
# 3. Probar (usar el endpoint que te da el comando anterior)

## Para desplegar en AWS (después de configurar la VM)

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

