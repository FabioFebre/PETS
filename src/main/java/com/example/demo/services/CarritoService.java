package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detallecarritoRepository;
    private final RestTemplate restTemplate;
    private final ProductoRepository productoRepository;
    private final ProductoCarritoRepository productoCarritoRepository;
    private static final String API_URL = "https://petsfriends-tw49.onrender.com/api/carritos/";


    public CarritoService(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository, CarritoRepository carritoRepository, DetalleCarritoRepository detallecarritoRepository, RestTemplate restTemplate, ProductoCarritoRepository productoCarritoRepository, ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.carritoRepository = carritoRepository;
        this.detallecarritoRepository = detallecarritoRepository;
        this.restTemplate = restTemplate;
        this.productoCarritoRepository = productoCarritoRepository;
        this.productoRepository = productoRepository;
    }

    public Carrito obtenerCarritoPorUsuario(Usuario usuario) {
        Optional<Carrito> carrito = carritoRepository.findByUsuario(usuario);

        if (!carrito.isPresent()) {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            carritoRepository.save(nuevoCarrito);
            return nuevoCarrito;
        }

        return carrito.get();
    }
    public Optional<Carrito> obtenerCarritoPorId(Long carritoId) {
        String url = API_URL + carritoId;
        ResponseEntity<Carrito> response = restTemplate.exchange(url, HttpMethod.GET, null, Carrito.class);
        return Optional.ofNullable(response.getBody());    }

    public List<DetalleVenta> obtenerProductosDelCarrito(Usuario usuario) {
        Carrito carrito = carritoRepository.findByUsuario_UsuarioId(usuario.getUsuarioId());
        return carrito != null ? detallecarritoRepository.findByCarrito(carrito) : List.of();
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



    public void vaciarCarrito(Carrito carrito) {
        carrito.getProductoCarrito().clear();
        carritoRepository.save(carrito);
    }

    public void agregarProducto(Long producto_id, Integer cantidad) {
        if (producto_id != null && cantidad != null) {
            System.out.println("Producto agregado: " + producto_id + ", Cantidad: " + cantidad);
        } else {
            System.out.println("Error: Datos incompletos");
        }
    }
    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public List<ProductoCarrito> obtenerProductosDelCarrito(Long id) {
        return null;
    }
    public void guardarCarrito(Carrito carrito) {
        carritoRepository.save(carrito);
    }

    public ProductoCarrito agregarProductoAlCarrito(ProductoCarrito productoCarrito) {
        return productoCarritoRepository.save(productoCarrito);
    }




}
