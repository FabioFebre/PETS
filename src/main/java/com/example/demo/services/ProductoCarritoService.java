package com.example.demo.services;

import com.example.demo.models.Carrito;
import com.example.demo.models.DetalleCarrito;
import com.example.demo.models.Producto;
import com.example.demo.models.ProductoCarrito;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.repository.ProductoCarritoRepository;
import com.example.demo.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductoCarritoService {

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public void agregarProductoAlCarrito(Long productoId, int cantidad, Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        BigDecimal precioTotal = producto.getPrecio().multiply(new BigDecimal(cantidad));

        Optional<ProductoCarrito> productoCarritoOpt = productoCarritoRepository.findByCarritoAndProducto(carrito, producto);

        ProductoCarrito productoCarrito;
        if (productoCarritoOpt.isPresent()) {
            productoCarrito = productoCarritoOpt.get();
            productoCarrito.setCantidad(productoCarrito.getCantidad() + cantidad);
        } else {
            productoCarrito = new ProductoCarrito();
            productoCarrito.setCarrito(carrito);
            productoCarrito.setProducto(producto);
            productoCarrito.setCantidad(cantidad);
        }

        productoCarritoRepository.save(productoCarrito);

        Optional<DetalleCarrito> detalleExistenteOpt = detalleCarritoRepository.findByCarritoAndProducto(carrito, producto);

        if (detalleExistenteOpt.isPresent()) {
            DetalleCarrito detalleExistente = detalleExistenteOpt.get();
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            detalleExistente.setPrecio(detalleExistente.getPrecio().add(precioTotal));
            detalleCarritoRepository.save(detalleExistente);
        } else {
            DetalleCarrito detalleCarrito = new DetalleCarrito();
            detalleCarrito.setCarrito(carrito);
            detalleCarrito.setProducto(producto);
            detalleCarrito.setCantidad(cantidad);
            detalleCarrito.setPrecio(precioTotal);
            detalleCarritoRepository.save(detalleCarrito);
        }
    }


    @Transactional
    public void eliminarProductosDelCarrito(Long carritoId) {
        productoCarritoRepository.deleteByCarritoId(carritoId);
    }
}
