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
        String url = "https://petsfriends-tw49.onrender.com/api/productos/";

        ResponseEntity<List<Producto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Producto>>() {}
        );

        return response.getBody();
    }



    public Producto obtenerProductoPorId(Long productoId) {
        if (productoId == null) {
            return null;
        }

        String url = "http://127.0.0.1:8000/api/productos/" + productoId + "/?format=json";
        try {
            ResponseEntity<Producto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Producto.class
            );
            Producto producto = response.getBody();
            System.out.println("Producto recibido: " + producto);
            return producto;
        } catch (Exception e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
            return null;
        }
    }

}
