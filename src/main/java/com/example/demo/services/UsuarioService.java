package com.example.demo.services;

import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;



    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }



    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);

        if (usuarioExistente == null) {
            throw new RuntimeException("Usuario con ID " + id + " no encontrado.");
        }

        usuarioExistente.setNombres(usuarioActualizado.getNombres());
        usuarioExistente.setApellidos(usuarioActualizado.getApellidos());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setContraseña(usuarioActualizado.getContraseña());
        usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
        usuarioExistente.setFoto(usuarioActualizado.getFoto());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());

        return usuarioRepository.save(usuarioExistente);
    }

}
