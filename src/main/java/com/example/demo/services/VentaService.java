package com.example.demo.services;

import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Venta;
import com.example.demo.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private final RestTemplate restTemplate;

    private final VentaRepository ventaRepository;

    @Autowired
    public VentaService(RestTemplate restTemplate, VentaRepository ventaRepository) {
        this.restTemplate = restTemplate;
        this.ventaRepository = ventaRepository;
    }






    public void guardarVenta(Venta venta) {
        ventaRepository.save(venta);
    }

}
