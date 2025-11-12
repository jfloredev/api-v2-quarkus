# 🚨 SOLUCIÓN AL ERROR: JAVA_HOME not defined

## El problema
```
The JAVA_HOME environment variable is not defined correctly
```

## ✅ Solución Rápida (opción 1 - recomendada)

Ejecuta el script de configuración automática:

```bash
chmod +x setup-vm.sh
./setup-vm.sh
source ~/.bashrc
```

Esto instalará:
- ✅ Java 17
- ✅ Node.js y npm
- ✅ Serverless Framework
- ✅ Maven
- ✅ Git

Después ejecuta:
```bash
./deploy.sh dev
```

---

## 🔧 Solución Manual (opción 2)

Si prefieres hacerlo paso a paso:

### 1. Instalar Java 17
```bash
sudo yum install java-17-amazon-corretto-devel -y
```

### 2. Configurar JAVA_HOME
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
export PATH=$JAVA_HOME/bin:$PATH
```

### 3. Hacer la configuración permanente
```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 4. Verificar
```bash
java -version
echo $JAVA_HOME
```

### 5. Instalar Node.js y Serverless
```bash
curl -sL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install nodejs -y
sudo npm install -g serverless
```

### 6. Ahora sí, desplegar
```bash
./deploy.sh dev
```

---

## 📋 Verificar que todo esté instalado

Ejecuta estos comandos para verificar:

```bash
java -version          # Debe mostrar Java 17
node --version         # Debe mostrar Node v18.x
npm --version          # Debe mostrar npm 9.x o superior
serverless --version   # Debe mostrar Serverless Framework
mvn --version          # Debe mostrar Maven
echo $JAVA_HOME        # Debe mostrar la ruta de Java
```

---

## 🎯 Comandos en orden (copia y pega todo)

```bash
# Configuración completa automática
chmod +x setup-vm.sh
./setup-vm.sh
source ~/.bashrc

# Verificar instalación
java -version
serverless --version

# Desplegar
chmod +x deploy.sh
./deploy.sh dev
```

---

## ❓ Si aún tienes problemas

1. **Verificar que Java esté instalado:**
   ```bash
   ls -la /usr/lib/jvm/
   ```

2. **Si no ves java-17-amazon-corretto, instálalo:**
   ```bash
   sudo yum install java-17-amazon-corretto-devel -y
   ```

3. **Configurar JAVA_HOME manualmente:**
   ```bash
   export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
   ```

4. **Intentar nuevamente:**
   ```bash
   ./deploy.sh dev
   ```

