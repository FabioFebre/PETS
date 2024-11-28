package com.example.demo.services;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dq2suwtlm");  
        config.put("api_key", "114247465137567");
        config.put("api_secret", "sirr5OuyYRfaNcd5Q7guSSfS_GE");
        this.cloudinary = new Cloudinary(config);
    }

    public String subirImagen(File archivo) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(archivo, new HashMap<>());
        return (String) uploadResult.get("url");
    }
}
