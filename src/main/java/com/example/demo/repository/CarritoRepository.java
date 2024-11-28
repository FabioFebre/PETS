package com.example.demo.repository;


import com.example.demo.models.Carrito;
import com.example.demo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Carrito findByUsuario_UsuarioId(int usuarioId);

    Optional<Carrito> findByUsuario(Usuario usuario);
}
