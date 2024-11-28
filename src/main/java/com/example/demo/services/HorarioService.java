package com.example.demo.services;

import com.example.demo.models.Horario;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;


    public List<Horario> obtenerTodosLosHorarios() {
        return horarioRepository.findAll();
    }

    // Obtener un horario por su ID
    public Horario obtenerHorarioPorId(Long horarioId) {
        return horarioRepository.findById(horarioId).orElse(null);
    }

}
