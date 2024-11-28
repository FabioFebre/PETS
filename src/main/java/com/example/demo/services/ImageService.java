package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${upload.dir}")
    private String uploadDir;

    public byte[] getImage(String imageName) {
        String imageUrl = "http://127.0.0.1:8000/media/productos/" + imageName;
        return restTemplate.getForObject(imageUrl, byte[].class);
    }

    public byte[] getFoto(String fotoName) {
        String imageUrl = "http://127.0.0.1:8000/media/fotos/" + fotoName;
        return restTemplate.getForObject(imageUrl, byte[].class);
    }




}
