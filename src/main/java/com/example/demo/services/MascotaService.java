package com.example.demo.services;

import com.example.demo.models.Mascota;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class MascotaService {

    public static final String API_URL = "https://petsfriends-tw49.onrender.com/api/mascotas/";


    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;

    public List<Mascota> obtenerMascotas() {
        Mascota[] mascotasArray = restTemplate.getForObject(API_URL, Mascota[].class);

        if (mascotasArray != null) {
            return Arrays.asList(mascotasArray);
        }
        return null;
    }
    public List<Mascota> obtenerMascotasPorUsuario(int usuarioId) {
        return mascotaRepository.findByUsuarioId(usuarioId);  // Cambié Long a int
    }


    public Mascota obtenerMascotaPorId(Long mascotaId) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .pathSegment(mascotaId.toString())
                .toUriString();
        return restTemplate.getForObject(url, Mascota.class);
    }

    public Mascota crearMascota(Mascota mascota) {
        return restTemplate.postForObject(API_URL, mascota, Mascota.class);
    }

    public void actualizarMascota(Long mascotaId, Mascota mascota) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .pathSegment(mascotaId.toString())
                .toUriString();
        restTemplate.put(url, mascota);
    }


    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }


}
