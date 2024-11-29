package com.example.demo.services;

import com.example.demo.models.Mascota;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {



    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;


    public List<Mascota> obtenerMascotasPorUsuario(int usuarioId) {
        return mascotaRepository.findByUsuarioId(usuarioId);
    }
    public List<Mascota> obtenerTodasLasMascotas() {
        return mascotaRepository.findAll();
    }

    public Optional<Mascota> obtenerMascotaPorId(Integer id) {
        return mascotaRepository.findById(id);
    }

    public Optional<Mascota> findById(Integer mascotaId) {
        return mascotaRepository.findById(mascotaId);
    }




    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }


}
