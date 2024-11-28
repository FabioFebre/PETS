package com.example.demo.services;

import com.example.demo.models.Veterinario;
import com.example.demo.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    @Autowired
    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    public Veterinario saveVeterinario(Veterinario veterinario) {
        return veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> getAllVeterinarios() {
        return veterinarioRepository.findAll();
    }

    public Optional<Veterinario> getVeterinarioById(Long id) {
        return veterinarioRepository.findById(id);
    }

    public void deleteVeterinario(Long id) {
        veterinarioRepository.deleteById(id);
    }


}
