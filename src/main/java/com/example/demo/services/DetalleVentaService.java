package com.example.demo.services;

import com.example.demo.models.Carrito;
import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Usuario;
import com.example.demo.models.Venta;
import com.example.demo.repository.DetalleVentaRepository;
import com.example.demo.repository.VentaRepository;  // Asegúrate de tener un repositorio para 'Venta'
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
    }

    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .map(venta -> {
                    venta.getDetalles().size();
                    return venta;
                })
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }


    public void guardarDetallesVenta(Carrito carrito, List<DetalleVenta> detalles) {
        for (DetalleVenta detalle : detalles) {
            detalle.setCarrito(carrito);
        }
        detalleVentaRepository.saveAll(detalles);
    }



    public List<Venta> listarDetallesPorUsuario(Usuario usuario) {
        List<Venta> ventas = ventaRepository.findByUsuario(usuario);

        for (Venta venta : ventas) {
            List<DetalleVenta> detallesVenta = detalleVentaRepository.findByVenta(venta);
            venta.setDetalles(detallesVenta);
        }
        return ventas;
    }





}
