package com.example.demo.services;

import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Carrito;
import com.example.demo.models.DetalleCarrito;
import com.example.demo.repository.DetalleCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleCarritoService {

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    public void agregarDetalleCarrito(DetalleCarrito detalleCarrito) {
        detalleCarritoRepository.save(detalleCarrito);
    }

}
