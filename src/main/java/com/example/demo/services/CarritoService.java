package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.repository.ProductoCarritoRepository;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detallecarritoRepository;

    private final ProductoRepository productoRepository;
    private final ProductoCarritoRepository productoCarritoRepository;

    public CarritoService(CarritoRepository carritoRepository, DetalleCarritoRepository detallecarritoRepository, ProductoCarritoRepository productoCarritoRepository, ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.detallecarritoRepository = detallecarritoRepository;
        this.productoCarritoRepository = productoCarritoRepository;
        this.productoRepository = productoRepository;
    }

    public Carrito obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    public Optional<Carrito> obtenerCarritoPorId(Long carritoId) {
        return carritoRepository.findById(carritoId);
    }

    public List<DetalleCarrito> obtenerProductosDelCarrito(Usuario usuario) {
        Carrito carrito = carritoRepository.findByUsuario_UsuarioId(usuario.getUsuarioId());
        return carrito != null ? detallecarritoRepository.findByCarrito(carrito) : List.of();
    }


    public BigDecimal calcularTotal(Usuario usuario) {
        Carrito carrito = carritoRepository.findByUsuario_UsuarioId(usuario.getUsuarioId());
        return carrito != null ? productoCarritoRepository.findByCarrito(carrito).stream()
                .map(pc -> pc.getProducto().getPrecio().multiply(BigDecimal.valueOf(pc.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
    }

    public void eliminarProductoDelCarrito(Usuario usuario, Long productoId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        productoOpt.ifPresent(producto -> {
            Optional<ProductoCarrito> productoCarritoOpt = productoCarritoRepository.findByCarritoAndProducto(carrito, producto);
            productoCarritoOpt.ifPresent(productoCarritoRepository::delete);
        });
    }

    public Carrito obtenerContenidoCarrito(Usuario usuario) {
        return obtenerCarritoPorUsuario(usuario);
    }

    public void actualizarCantidadProducto(Usuario usuario, Long productoId, int cantidad) {
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        productoOpt.ifPresent(producto -> {
            Optional<ProductoCarrito> productoCarritoOpt = productoCarritoRepository.findByCarritoAndProducto(carrito, producto);
            productoCarritoOpt.ifPresent(productoCarrito -> {
                productoCarrito.setCantidad(cantidad);
                productoCarritoRepository.save(productoCarrito);
            });
        });
    }

    @PostMapping
    public ResponseEntity<ProductoCarrito> agregarProductoAlCarrito(@RequestBody ProductoCarrito productoCarrito) {
        ProductoCarrito nuevoProductoCarrito = productoCarritoRepository.save(productoCarrito);
        return ResponseEntity.ok(nuevoProductoCarrito);
    }

    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carrito.getProductoCarrito().clear();

        carritoRepository.save(carrito);
    }

    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }
}
