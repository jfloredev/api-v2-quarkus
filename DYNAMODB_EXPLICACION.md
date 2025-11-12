# Por qué DynamoDB usa "Tablas" siendo NoSQL

## Explicación

Aunque **DynamoDB es una base de datos NoSQL**, utiliza el término **"tabla"** (table) como contenedor lógico para organizar los datos. Esto puede parecer confuso al principio, pero hay razones importantes:

### Diferencias con SQL tradicional

| Aspecto | SQL (Relacional) | DynamoDB (NoSQL) |
|---------|------------------|------------------|
| **Esquema** | Rígido, definido previamente | Flexible, cada ítem puede tener diferentes atributos |
| **Clave primaria** | Obligatoria | Obligatoria (Partition Key + Sort Key opcional) |
| **Relaciones** | JOINs entre tablas | Sin JOINs, desnormalización |
| **Estructura** | Filas y columnas fijas | Ítems (documentos JSON) |
| **Índices** | Múltiples índices | Global/Local Secondary Indexes |

### ¿Por qué "tabla" en DynamoDB?

1. **Herencia conceptual**: AWS mantiene terminología familiar para facilitar la adopción
2. **Contenedor lógico**: La "tabla" es simplemente un espacio de nombres para agrupar ítems relacionados
3. **Unidad de facturación**: AWS cobra por tabla, no por base de datos
4. **Configuración de capacidad**: Cada tabla tiene su propia configuración de throughput

### Nombres alternativos que podrías usar en tu código

En nuestro código, usamos `COLLECTION_NAME` en lugar de `TABLE_NAME` para reflejar mejor la naturaleza NoSQL:

```java
// Más descriptivo para NoSQL
private static final String COLLECTION_NAME = "usuarios";

// En lugar de
private static final String TABLE_NAME = "Usuarios";
```

### Ejemplo de estructura en DynamoDB

```json
{
  "TableName": "usuarios",
  "Items": [
    {
      "id": "123",
      "nombre": "Juan",
      "email": "juan@example.com"
    },
    {
      "id": "456",
      "nombre": "María",
      "email": "maria@example.com",
      "telefono": "+1234567890"  // ← Atributo extra, sin problema
    }
  ]
}
```

Nota cómo el segundo ítem tiene un atributo `telefono` que el primero no tiene. Esto es **imposible en SQL** pero totalmente válido en DynamoDB.

### Conclusión

- **Término oficial AWS**: "Table"
- **Realidad técnica**: Es más una "colección de documentos" (como MongoDB)
- **En tu código**: Puedes usar `COLLECTION_NAME` para claridad conceptual
- **API de AWS**: Siempre usa el parámetro `tableName` (no podemos cambiarlo)

El código que creamos usa `COLLECTION_NAME` como constante para dejar claro que estamos trabajando con una estructura NoSQL flexible, aunque la API de DynamoDB requiera el término "tableName".

