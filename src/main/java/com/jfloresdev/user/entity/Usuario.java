package com.jfloresdev.user.entity;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Usuario {

    private String id;
    private String nombre;
    private String email;

    public Usuario() {
    }

    public Usuario(String nombre, String email) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.email = email;
    }

    public Usuario(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Convertir a DynamoDB Map
    public Map<String, AttributeValue> toDynamoDBMap() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("nombre", AttributeValue.builder().s(nombre).build());
        item.put("email", AttributeValue.builder().s(email).build());
        return item;
    }

    // Crear desde DynamoDB Map
    public static Usuario fromDynamoDBMap(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        Usuario usuario = new Usuario();
        if (item.containsKey("id")) {
            usuario.setId(item.get("id").s());
        }
        if (item.containsKey("nombre")) {
            usuario.setNombre(item.get("nombre").s());
        }
        if (item.containsKey("email")) {
            usuario.setEmail(item.get("email").s());
        }
        return usuario;
    }
}

