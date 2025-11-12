package com.jfloresdev.user.controller;

import com.jfloresdev.user.entity.Usuario;
import com.jfloresdev.user.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;

    /**
     * Endpoint para crear un nuevo usuario
     * POST /api/usuarios
     */
    @POST
    public Response agregarUsuario(Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.agregarUsuario(usuario);
            return Response.status(Response.Status.CREATED)
                .entity(nuevoUsuario)
                .build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
        }
    }

    /**
     * Endpoint para obtener un usuario por ID
     * GET /api/usuarios/{id}
     */
    @GET
    @Path("/{id}")
    public Response obtenerUsuarioPorId(@PathParam("id") String id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
        }
    }

    /**
     * Endpoint para obtener todos los usuarios
     * GET /api/usuarios
     */
    @GET
    public Response obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return Response.ok(usuarios).build();
    }

    /**
     * Endpoint para buscar un usuario por email
     * GET /api/usuarios/email/{email}
     */
    @GET
    @Path("/email/{email}")
    public Response obtenerUsuarioPorEmail(@PathParam("email") String email) {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);

        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
        }
    }

    /**
     * Endpoint para inicializar la tabla (solo para desarrollo/testing)
     * POST /api/usuarios/init
     */
    @POST
    @Path("/init")
    public Response inicializarTabla() {
        try {
            usuarioService.crearTablaUsuarios();
            Map<String, String> mensaje = new HashMap<>();
            mensaje.put("mensaje", "Tabla inicializada correctamente");
            return Response.ok()
                .entity(mensaje)
                .build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
        }
    }
}

