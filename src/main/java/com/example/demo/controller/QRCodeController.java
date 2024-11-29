package com.example.demo.controller;

import com.example.demo.models.Mascota;
import com.example.demo.services.MascotaService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QRCodeController {

    @Autowired
    private MascotaService mascotaService; // Inyectamos el servicio

    @GetMapping("/generar_qr/{mascotaId}")
    public void generarQRCodeConLogo(@PathVariable("mascotaId") Integer mascotaId, HttpServletResponse response) throws Exception {
        // Obtener la mascota por su ID
        Mascota mascota = mascotaService.obtenerMascotaPorId(mascotaId).orElse(null);

        if (mascota != null) {
            // Generar el código QR con logo
            byte[] qrCodeImage = mascota.generarCodigoQRConLogo();

            // Configurar la respuesta HTTP para devolver la imagen del QR
            response.setContentType("image/png");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=mascota_qr.png");

            // Escribir la imagen QR en la respuesta
            response.getOutputStream().write(qrCodeImage);
            response.getOutputStream().flush();
        } else {
            // Si no se encuentra la mascota, devolver un error 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Mascota no encontrada.");
        }
    }

}
