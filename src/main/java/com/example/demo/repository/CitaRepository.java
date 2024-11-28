package com.example.demo.repository;

import com.example.demo.models.Cita;
import com.example.demo.models.Horario;
import com.example.demo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuario(Usuario usuario);

    boolean existsByFechaCitaAndHorario(LocalDate fechaCita, Horario horario);
}
