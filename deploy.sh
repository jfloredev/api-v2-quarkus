#!/bin/bash

# Script de despliegue para AWS Lambda
set -e

echo "🚀 Desplegando API de Usuarios a AWS Lambda"
echo "==========================================="
echo ""

# Detectar y configurar JAVA_HOME si no está configurado
if [ -z "$JAVA_HOME" ]; then
    echo "⚙️  Configurando JAVA_HOME..."

    # Intentar encontrar Java en ubicaciones comunes
    if [ -d "/usr/lib/jvm/java-17" ]; then
        export JAVA_HOME=/usr/lib/jvm/java-17
    elif [ -d "/usr/lib/jvm/java-17-amazon-corretto" ]; then
        export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
    elif [ -d "/usr/lib/jvm/java-17-openjdk" ]; then
        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
    elif command -v java &> /dev/null; then
        # Intentar detectar desde el comando java
        JAVA_PATH=$(which java)
        export JAVA_HOME=$(dirname $(dirname $(readlink -f $JAVA_PATH)))
    else
        echo "❌ Java 17 no encontrado"
        echo "📦 Instala Java 17 con:"
        echo "   sudo yum install java-17-amazon-corretto-devel -y"
        exit 1
    fi

    export PATH=$JAVA_HOME/bin:$PATH
    echo "✅ JAVA_HOME configurado: $JAVA_HOME"
else
    echo "✅ JAVA_HOME ya configurado: $JAVA_HOME"
fi

# Verificar versión de Java
echo "☕ Verificando Java..."
java -version
echo ""

# Verificar que serverless esté instalado
if ! command -v serverless &> /dev/null; then
    echo "❌ Serverless Framework no está instalado"
    echo "📦 Instala con: npm install -g serverless"
    exit 1
fi

echo "✅ Serverless Framework encontrado"
echo ""

# Limpiar compilaciones anteriores
echo "🧹 Limpiando compilaciones anteriores..."
./mvnw clean
echo ""

# Compilar el proyecto
echo "📦 Compilando proyecto Quarkus para Lambda..."
./mvnw package -DskipTests
echo ""

# Verificar que el archivo function.zip existe
if [ ! -f "target/function.zip" ]; then
    echo "❌ No se encontró target/function.zip"
    echo "Verifica que la compilación haya sido exitosa"
    exit 1
fi

echo "✅ Compilación exitosa - function.zip creado"
echo ""

# Obtener el stage (dev, prod, etc)
STAGE=${1:-dev}
echo "🌍 Desplegando a stage: $STAGE"
echo "📋 Tabla DynamoDB: ${STAGE}-usuarios"
echo ""

# Desplegar con Serverless
echo "🚀 Desplegando a AWS..."
serverless deploy --stage $STAGE

echo ""
echo "✅ Despliegue completado!"
echo ""
echo "📝 Próximos pasos:"
echo "  1. Copia el endpoint URL que se muestra arriba"
echo "  2. La tabla '${STAGE}-usuarios' ya fue creada automáticamente"
echo "  3. Prueba creando un usuario con curl"
echo ""
echo "Ejemplo:"
echo "  export API_URL=<tu-endpoint-url>"
echo "  curl -X POST \$API_URL/api/usuarios -H 'Content-Type: application/json' -d '{\"nombre\":\"Juan\",\"email\":\"juan@test.com\"}'"
echo ""

