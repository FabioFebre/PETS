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

    public List<MetodoPago> obtenerTodosLosMetodos() {
        return metodoPagoRepository.findAll();
    }

    public Optional<MetodoPago> obtenerMetodoPorId(Long id) {
        return metodoPagoRepository.findById(id);
    }


    public MetodoPago crearMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    public MetodoPago actualizarMetodoPago(Long id, MetodoPago metodoPagoActualizado) {
        return metodoPagoRepository.findById(id)
                .map(metodo -> {
                    metodo.setNombre(metodoPagoActualizado.getNombre());
                    return metodoPagoRepository.save(metodo);
                })
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }

    public void eliminarMetodoPago(Long id) {
        metodoPagoRepository.deleteById(id);
    }
}
