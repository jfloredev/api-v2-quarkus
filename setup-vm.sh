#!/bin/bash

# Script de configuración inicial para máquina virtual EC2
echo "🔧 Configurando entorno para API de Usuarios"
echo "============================================="
echo ""

# Actualizar sistema
echo "📦 Actualizando sistema..."
sudo yum update -y
echo ""

# Instalar Java 17
echo "☕ Instalando Java 17..."
sudo yum install java-17-amazon-corretto-devel -y
echo ""

# Configurar JAVA_HOME permanentemente
echo "⚙️  Configurando JAVA_HOME..."
JAVA_HOME_PATH="/usr/lib/jvm/java-17-amazon-corretto"

if [ ! -f ~/.bashrc.backup ]; then
    cp ~/.bashrc ~/.bashrc.backup
fi

if ! grep -q "JAVA_HOME" ~/.bashrc; then
    echo "" >> ~/.bashrc
    echo "# Java Configuration" >> ~/.bashrc
    echo "export JAVA_HOME=$JAVA_HOME_PATH" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
fi

export JAVA_HOME=$JAVA_HOME_PATH
export PATH=$JAVA_HOME/bin:$PATH

echo "✅ Java instalado:"
java -version
echo ""

# Instalar Node.js y npm (necesario para Serverless)
echo "📦 Instalando Node.js..."
curl -sL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install nodejs -y
echo ""

echo "✅ Node.js instalado:"
node --version
npm --version
echo ""

# Instalar Serverless Framework
echo "🚀 Instalando Serverless Framework..."
sudo npm install -g serverless
echo ""

echo "✅ Serverless instalado:"
serverless --version
echo ""

# Instalar Maven (si no está instalado)
if ! command -v mvn &> /dev/null; then
    echo "📦 Instalando Maven..."
    sudo yum install maven -y
    echo ""
fi

echo "✅ Maven instalado:"
mvn --version
echo ""

# Instalar Git (si no está instalado)
if ! command -v git &> /dev/null; then
    echo "📦 Instalando Git..."
    sudo yum install git -y
    echo ""
fi

echo "✅ Git instalado:"
git --version
echo ""

echo ""
echo "✅ Configuración completada!"
echo ""
echo "📝 Próximos pasos:"
echo "  1. Reinicia la sesión o ejecuta: source ~/.bashrc"
echo "  2. Clona el repositorio: git clone <tu-repo>"
echo "  3. cd api-v2-quarkus"
echo "  4. chmod +x deploy.sh"
echo "  5. ./deploy.sh dev"
echo ""

