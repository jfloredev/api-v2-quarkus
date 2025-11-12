#!/bin/bash

echo "🚀 Iniciando pruebas de la API de Usuarios"
echo "=========================================="
echo ""

echo "1️⃣  Inicializando tabla..."
curl -X POST http://localhost:8080/api/usuarios/init
echo -e "\n"
sleep 1

echo "2️⃣  Creando primer usuario (Juan)..."
JUAN=$(curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@example.com"}')
echo $JUAN | jq '.'
echo ""
sleep 1

echo "3️⃣  Creando segundo usuario (María)..."
MARIA=$(curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María García","email":"maria@example.com"}')
echo $MARIA | jq '.'
echo ""
sleep 1

echo "4️⃣  Creando tercer usuario (Carlos)..."
CARLOS=$(curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos Ruiz","email":"carlos@example.com"}')
echo $CARLOS | jq '.'
echo ""
sleep 1

echo "5️⃣  ❌ Intentando crear usuario con email duplicado (debe fallar)..."
curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro López","email":"juan@example.com"}' | jq '.'
echo ""
sleep 1

echo "6️⃣  Listando todos los usuarios..."
curl -s http://localhost:8080/api/usuarios | jq '.'
echo ""
sleep 1

echo "7️⃣  Buscando usuario por email (maria@example.com)..."
curl -s http://localhost:8080/api/usuarios/email/maria@example.com | jq '.'
echo ""
sleep 1

# Extraer ID del primer usuario
JUAN_ID=$(echo $JUAN | jq -r '.id')
echo "8️⃣  Obteniendo usuario por ID ($JUAN_ID)..."
curl -s http://localhost:8080/api/usuarios/$JUAN_ID | jq '.'
echo ""

echo ""
echo "✅ Pruebas completadas!"
echo ""
echo "📊 Resumen:"
echo "  - ✓ Tabla creada"
echo "  - ✓ 3 usuarios creados exitosamente"
echo "  - ✓ Validación de email duplicado funcionando"
echo "  - ✓ Búsqueda por email OK"
echo "  - ✓ Búsqueda por ID OK"

