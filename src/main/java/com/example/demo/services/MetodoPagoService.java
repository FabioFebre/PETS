package com.example.demo.services;

import com.example.demo.models.MetodoPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class MetodoPagoService {

    private final RestTemplate restTemplate;

    private final String API_URL = "https://petsfriends-tw49.onrender.com/api/metodos-pago/";

    @Autowired
    public MetodoPagoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MetodoPago> obtenerTodosLosMetodos() {
        MetodoPago[] metodos = restTemplate.getForObject(API_URL, MetodoPago[].class);
        return List.of(metodos);
    }

    public MetodoPago obtenerMetodoPorId(Long id) {
        String url = API_URL + id;
        return restTemplate.getForObject(url, MetodoPago.class);
    }

}
