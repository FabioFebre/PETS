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
    public void generarQRCode(@PathVariable("mascotaId") Integer mascotaId, HttpServletResponse response) throws Exception {
        Mascota mascota = mascotaService.obtenerMascotaPorId(mascotaId).orElse(null);

        if (mascota != null) {
            // Generar el código QR utilizando el método que ya tienes
            byte[] qrCodeImage = mascota.generarCodigoQR();

            response.setContentType("image/png");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=mascota_qr.png");

            response.getOutputStream().write(qrCodeImage);
            response.getOutputStream().flush();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Mascota no encontrada.");
        }
    }
}
