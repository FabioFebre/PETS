package com.example.demo.repository;

import com.example.demo.models.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    List<Mascota> findByUsuarioId(int usuarioId);  // Cambié Long a int

    Mascota findByNombreAndUsuarioId(String nombre, int usuarioId);


}
