package com.example.demo.services;

import com.example.demo.models.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private final RestTemplate restTemplate;

    public ProductoService(ProductoRepository productoRepository, RestTemplate restTemplate) {
        this.productoRepository = productoRepository;
        this.restTemplate = restTemplate;
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .filter(p -> p.getStock() > 2 && p.getEstado())
                .collect(Collectors.toList());
    }
}
