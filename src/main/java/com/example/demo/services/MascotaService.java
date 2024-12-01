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


    public void eliminarMascota(Integer mascotaId) throws Exception {
        Optional<Mascota> mascota = mascotaRepository.findById(mascotaId);
        if (mascota.isPresent()) {
            mascotaRepository.delete(mascota.get());
        } else {
            throw new Exception("Mascota no encontrada");
        }
    }


    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }


    public Mascota actualizarMascota(int id, Mascota mascotaActualizada) {
        Mascota mascotaExistente = mascotaRepository.findById(id).orElse(null);

        if (mascotaExistente == null) {
            throw new RuntimeException("Mascota con ID " + id + " no encontrada.");
        }

        mascotaExistente.setNombre(mascotaActualizada.getNombre());
        mascotaExistente.setEspecie(mascotaActualizada.getEspecie());
        mascotaExistente.setRaza(mascotaActualizada.getRaza());
        mascotaExistente.setFechaNacimiento(mascotaActualizada.getFechaNacimiento());
        mascotaExistente.setPeso(mascotaActualizada.getPeso());
        mascotaExistente.setAltura(mascotaActualizada.getAltura());
        mascotaExistente.setEdad(mascotaActualizada.getEdad());
        mascotaExistente.setColor(mascotaActualizada.getColor());
        mascotaExistente.setFotom(mascotaActualizada.getFotom());
        mascotaExistente.setObservaciones(mascotaActualizada.getObservaciones());

        // Guardamos y retornamos la mascota actualizada
        return mascotaRepository.save(mascotaExistente);
    }


}
