package com.example.demo.services;

import com.example.demo.models.Carrito;
import com.example.demo.models.DetalleCarrito;
import com.example.demo.models.ProductoCarrito;

import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.repository.ProductoCarritoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductoCarritoService {

    private final RestTemplate restTemplate;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;
    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    public ProductoCarritoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductoCarrito> obtenerProductosCarrito(Long carritoId, int usuarioId) {
        String url = "https://petsfriends-tw49.onrender.com/api/productos-carrito/?carritoId=" + carritoId + "&usuarioId=" + usuarioId + "&format=json";
        ProductoCarrito[] productosArray = restTemplate.getForObject(url, ProductoCarrito[].class);

        return Arrays.asList(productosArray);
    }


    public void guardarProductoCarrito(ProductoCarrito productoCarrito) {
        productoCarritoRepository.save(productoCarrito);
    }




    @Transactional
    public void eliminarProductoDelCarrito(Carrito carrito, Long producto_id) {
        productoCarritoRepository.deleteByCarritoIdAndProductoId(carrito.getId(), producto_id);
    }


    @Transactional
    public void eliminarProductosDelCarritoId(Long carritoId) {
        productoCarritoRepository.deleteByCarritoId(carritoId);
    }


    @Transactional
    public ProductoCarrito agregarProductoAlCarrito(ProductoCarrito productoCarrito) {
        ProductoCarrito productoGuardado = productoCarritoRepository.save(productoCarrito);

        DetalleCarrito detalleCarrito = new DetalleCarrito();
        detalleCarrito.setCarrito(productoCarrito.getCarrito());
        detalleCarrito.setProducto(productoCarrito.getProducto());
        detalleCarrito.setCantidad(productoCarrito.getCantidad());

        detalleCarritoRepository.save(detalleCarrito);

        return productoGuardado;
    }



}
