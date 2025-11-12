package com.jfloresdev.user.service;

import com.jfloresdev.user.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UsuarioService {

    // Nombre de la colección/tabla en DynamoDB (desde variable de entorno o default)
    private static final String COLLECTION_NAME = System.getenv().getOrDefault("TABLE_NAME", "dev-usuarios");
    private static final String EMAIL_INDEX = "email-index";

    @Inject
    DynamoDbClient dynamoDB;

    /**
     * Crea la tabla de usuarios si no existe
     */
    public void crearTablaUsuarios() {
        try {
            // Verificar si la colección ya existe
            dynamoDB.describeTable(builder -> builder.tableName(COLLECTION_NAME));
            System.out.println("La colección de usuarios ya existe");
        } catch (ResourceNotFoundException e) {
            // Crear la colección si no existe
            dynamoDB.createTable(builder -> builder
                .tableName(COLLECTION_NAME)
                .keySchema(
                    KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build()
                )
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                    AttributeDefinition.builder()
                        .attributeName("email")
                        .attributeType(ScalarAttributeType.S)
                        .build()
                )
                .globalSecondaryIndexes(
                    GlobalSecondaryIndex.builder()
                        .indexName(EMAIL_INDEX)
                        .keySchema(
                            KeySchemaElement.builder()
                                .attributeName("email")
                                .keyType(KeyType.HASH)
                                .build()
                        )
                        .projection(Projection.builder()
                            .projectionType(ProjectionType.ALL)
                            .build())
                        .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder()
                    .readCapacityUnits(5L)
                    .writeCapacityUnits(5L)
                    .build())
            );
            System.out.println("Colección de usuarios creada exitosamente");
        }
    }

    /**
     * Verifica si un email ya existe en la base de datos
     */
    public boolean existeEmail(String email) {
        try {
            QueryRequest queryRequest = QueryRequest.builder()
                .tableName(COLLECTION_NAME)
                .indexName(EMAIL_INDEX)
                .keyConditionExpression("email = :email")
                .expressionAttributeValues(Map.of(
                    ":email", AttributeValue.builder().s(email).build()
                ))
                .build();

            QueryResponse response = dynamoDB.query(queryRequest);
            return response.count() > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Agrega un nuevo usuario
     */
    public Usuario agregarUsuario(Usuario usuario) throws Exception {
        // Validar que los campos no estén vacíos
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new Exception("El email no puede estar vacío");
        }

        // Verificar que el email no exista
        if (existeEmail(usuario.getEmail())) {
            throw new Exception("Ya existe un usuario con este email: " + usuario.getEmail());
        }

        // Guardar en DynamoDB
        PutItemRequest putItemRequest = PutItemRequest.builder()
            .tableName(COLLECTION_NAME)
            .item(usuario.toDynamoDBMap())
            .build();

        dynamoDB.putItem(putItemRequest);
        return usuario;
    }

    /**
     * Obtiene un usuario por su ID
     */
    public Usuario obtenerUsuarioPorId(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
            .tableName(COLLECTION_NAME)
            .key(key)
            .build();

        GetItemResponse response = dynamoDB.getItem(getItemRequest);

        if (response.hasItem()) {
            return Usuario.fromDynamoDBMap(response.item());
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        ScanRequest scanRequest = ScanRequest.builder()
            .tableName(COLLECTION_NAME)
            .build();

        ScanResponse response = dynamoDB.scan(scanRequest);

        for (Map<String, AttributeValue> item : response.items()) {
            usuarios.add(Usuario.fromDynamoDBMap(item));
        }

        return usuarios;
    }

    /**
     * Obtiene un usuario por su email
     */
    public Usuario obtenerUsuarioPorEmail(String email) {
        QueryRequest queryRequest = QueryRequest.builder()
            .tableName(COLLECTION_NAME)
            .indexName(EMAIL_INDEX)
            .keyConditionExpression("email = :email")
            .expressionAttributeValues(Map.of(
                ":email", AttributeValue.builder().s(email).build()
            ))
            .build();

        QueryResponse response = dynamoDB.query(queryRequest);

        if (response.count() > 0) {
            return Usuario.fromDynamoDBMap(response.items().get(0));
        }
        return null;
    }

}

