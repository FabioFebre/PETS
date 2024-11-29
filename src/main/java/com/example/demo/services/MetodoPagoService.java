package com.example.demo.services;

import com.example.demo.models.MetodoPago;
import com.example.demo.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    @Autowired
    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    // Obtener todos los métodos de pago
    public List<MetodoPago> obtenerTodosLosMetodos() {
        return metodoPagoRepository.findAll();
    }

    // Obtener un método de pago por ID
    public MetodoPago obtenerMetodoPorId(Long id) {
        Optional<MetodoPago> metodo = metodoPagoRepository.findById(id);
        return metodo.orElse(null); // Devuelve null si no se encuentra el método de pago
    }

    // Agregar un nuevo método de pago
    public MetodoPago agregarMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    // Actualizar un método de pago existente
    public MetodoPago actualizarMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    // Eliminar un método de pago por ID
    public void eliminarMetodoPago(Long id) {
        metodoPagoRepository.deleteById(id);
    }
}
