package com.example.demo.services;

import com.example.demo.models.Cita;
import com.example.demo.models.Usuario;
import com.example.demo.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    @Autowired
    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }


    public List<Cita> obtenerCitasPorUsuario(Usuario usuario) {
        return citaRepository.findByUsuario(usuario);
    }


    public Cita createCita(Cita cita) throws Exception {
        if (citaRepository.existsByFechaCitaAndHorario(cita.getFechaCita(), cita.getHorario())) {
            throw new Exception("Ya existe una cita en esa fecha y horario.");
        }
        return citaRepository.save(cita);
    }

    public Cita updateCita(Long citaId, Cita nuevaCita) throws Exception {
        return citaRepository.findById(citaId).map(cita -> {
            cita.setUsuario(nuevaCita.getUsuario());
            cita.setMascota(nuevaCita.getMascota());
            cita.setServicio(nuevaCita.getServicio());
            cita.setRazon(nuevaCita.getRazon());
            cita.setObservaciones(nuevaCita.getObservaciones());
            cita.setFechaCita(nuevaCita.getFechaCita());
            cita.setHorario(nuevaCita.getHorario());
            cita.setEstado(nuevaCita.getEstado());
            return citaRepository.save(cita);
        }).orElseThrow(() -> new Exception("Cita no encontrada"));
    }

    public void eliminarCita(Long citaId) throws Exception {
        Optional<Cita> cita = citaRepository.findById(citaId);
        if (cita.isPresent()) {
            citaRepository.delete(cita.get());
        } else {
            throw new Exception("Cita no encontrada");
        }
    }


}
