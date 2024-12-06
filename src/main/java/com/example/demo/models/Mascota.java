package com.example.demo.models;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "api_mascota")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mascota_id")
    private Integer mascotaId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 50)
    private String especie;

    @Column(name = "raza", nullable = false, length = 100)
    private String raza;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "peso", precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "altura", precision = 5, scale = 2)
    private BigDecimal altura;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "genero", length = 50, nullable = true)
    private String genero;

    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Column(name = "fotom")
    private String fotom;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "codigo_identificacion", length = 10, unique = true, updatable = false)
    private String codigoIdentificacion;

    // Getters y Setters
    public Integer getMascotaId() {
        return mascotaId;
    }
    // Constructor sin argumentos
    public Mascota() {}
    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFotom() {
        return fotom;
    }

    public void setFotom(String fotom) {
        this.fotom = fotom;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCodigoIdentificacion() {
        return codigoIdentificacion;
    }

    @PrePersist
    public void generarCodigoIdentificacion() {
        if (codigoIdentificacion == null) {
            this.codigoIdentificacion = UUID.randomUUID().toString().substring(0, 8);  // Genera un código de 8 caracteres
        }
        if (fechaInscripcion == null) {
            this.fechaInscripcion = LocalDateTime.now();
        }
    }

    public byte[] generarCodigoQR() throws WriterException, IOException {
        // Obtenemos los datos de la mascota
        String nombre = this.nombre;
        String edad = this.edad != null ? String.valueOf(this.edad) : "Desconocida";
        String raza = this.raza != null ? this.raza : "Desconocida";
        String codigoIdentificacion = this.codigoIdentificacion;

        // Concatenamos los datos para crear un string representativo de la mascota
        String data = "Este es el codigo de Identificacion de tu Mascota : " + codigoIdentificacion ;

        // Configuramos los parámetros de generación del código QR
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);  // Espacio en blanco alrededor del QR

        // Generar el código QR como imagen
        BufferedImage qrImage = toBufferedImage(qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 180, 180, hints));

        // Convertir la imagen del QR a un arreglo de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);

        return baos.toByteArray();
    }

    // Método para convertir el BitMatrix en una imagen BufferedImage
    private BufferedImage toBufferedImage(com.google.zxing.common.BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        return image;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
