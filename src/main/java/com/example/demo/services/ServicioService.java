package com.example.demo.services;


import com.example.demo.models.Servicio;
import com.example.demo.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }
    public Servicio obtenerServicioPorId(Long servicioId) {
        return servicioRepository.findById(servicioId).orElse(null);
    }
    public List<Servicio> obtenerTodosLosServicios() {
        return servicioRepository.findAll();
    }
}
