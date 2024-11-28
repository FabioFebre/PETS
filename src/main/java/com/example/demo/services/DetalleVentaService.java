package com.example.demo.services;

import com.example.demo.models.Carrito;
import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Usuario;
import com.example.demo.repository.DetalleVentaRepository;
import com.example.demo.repository.VentaRepository;  // Asegúrate de tener un repositorio para 'Venta'
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository; // Necesitamos el repositorio de ventas

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
    }

    public void guardarDetallesVenta(Carrito carrito, List<DetalleVenta> detalles) {
        for (DetalleVenta detalle : detalles) {
            detalle.setCarrito(carrito);
        }
        detalleVentaRepository.saveAll(detalles);
    }

    public List<DetalleVenta> listarVentasPorUsuario(Usuario usuario) {
        // Obtener todas las ventas asociadas al usuario
        List<DetalleVenta> ventasPorUsuario = detalleVentaRepository.findByVentaUsuario(usuario);
        return ventasPorUsuario;
    }
}
