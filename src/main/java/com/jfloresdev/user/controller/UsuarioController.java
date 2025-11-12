package com.jfloresdev.user.controller;

import com.jfloresdev.user.entity.Usuario;
import com.jfloresdev.user.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
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
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Usuario no encontrado"))
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



}

