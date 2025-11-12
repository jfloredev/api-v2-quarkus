package com.jfloresdev.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@ApplicationScoped
public class DynamoDBConfig {

    @Produces
    @ApplicationScoped
    public DynamoDbClient dynamoDbClient() {
        // Para desarrollo local, puedes usar DynamoDB Local
        String endpoint = System.getenv("DYNAMODB_ENDPOINT");

        if (endpoint != null && !endpoint.isEmpty()) {
            // Configuración para DynamoDB Local
            return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .build();
        } else {
            // Configuración para AWS DynamoDB en la nube
            return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        }
    }
}

