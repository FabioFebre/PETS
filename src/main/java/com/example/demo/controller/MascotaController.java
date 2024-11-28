package com.example.demo.controller;

import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Mascota;
import com.example.demo.models.Usuario;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.services.*;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private RestTemplate restTemplate;

    private final UsuarioService usuarioService;
    private final DetalleVentaService detalleVentaService;

    private final CloudinaryService cloudinaryService;


    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaController(UsuarioService usuarioService, DetalleVentaService detalleVentaService, CloudinaryService cloudinaryService) {
        this.usuarioService = usuarioService;
        this.detalleVentaService = detalleVentaService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            String apiUrl = "https://petsfriends-tw49.onrender.com/api/mascotas/?usuario_id=" + usuario.getUsuarioId();
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();

            ResponseEntity<List<Mascota>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Mascota>>() {});

            String imagenUrl = usuario.getFoto();
            if (imagenUrl == null || imagenUrl.isEmpty()) {
                imagenUrl = "https://res.cloudinary.com/dq2suwtlm/image/upload/v1732582308/user.webp";
            }

            List<Mascota> mascotas = response.getBody();

            for (Mascota mascota : mascotas) {
                if (mascota.getFotom() == null || mascota.getFotom().isEmpty()) {
                    mascota.setFotom("https://res.cloudinary.com/dq2suwtlm/image/upload/v1732582308/default_mascota.webp");
                }
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("mascotas", mascotas);
            model.addAttribute("imagenUrl", imagenUrl);

            return "perfil";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al perfil.");
            return "login";
        }
    }



    @GetMapping("/mascota/nueva")
    public String mostrarFormularioCrearMascota(HttpSession session, Model model, Object mascota) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("mascota", new Mascota());
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioId", usuario.getUsuarioId());

            return "crear_mascota";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al perfil.");
            return "login";
        }

    }
    @PostMapping("/mascota/crear")
    public String crearMascota(@RequestParam("nombre") String nombre,
                               @RequestParam("especie") String especie,
                               @RequestParam("raza") String raza,
                               @RequestParam("fechaNacimiento") String fechaNacimiento,
                               @RequestParam("peso") BigDecimal peso,
                               @RequestParam("altura") BigDecimal altura,
                               @RequestParam("edad") Integer edad,
                               @RequestParam("color") String color,
                               @RequestParam("observaciones") String observaciones,
                               @RequestParam(value = "fotom", required = false) MultipartFile fotom, // Fotom es opcional
                               HttpSession session,
                               Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            model.addAttribute("error", "Debes iniciar sesión para registrar una mascota.");
            return "login";
        }

        Mascota mascotaExistente = mascotaRepository.findByNombreAndUsuarioId(nombre, usuario.getUsuarioId());
        if (mascotaExistente != null) {
            model.addAttribute("error", "Ya tienes una mascota registrada con ese nombre.");
            return "crear_mascota";
        }

        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre(nombre);
        nuevaMascota.setEspecie(especie);
        nuevaMascota.setRaza(raza);

        if (!fechaNacimiento.isEmpty()) {
            nuevaMascota.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
        }
        nuevaMascota.setPeso(peso);
        nuevaMascota.setAltura(altura);
        nuevaMascota.setEdad(edad);
        nuevaMascota.setColor(color);
        nuevaMascota.setObservaciones(observaciones);
        nuevaMascota.setUsuarioId(usuario.getUsuarioId());

        if (fotom != null && !fotom.isEmpty()) {
            try {
                File archivo = new File(fotom.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(fotom.getBytes());
                fos.close();

                String imagenUrlMascota = cloudinaryService.subirImagen(archivo);

                nuevaMascota.setFotom(imagenUrlMascota);

            } catch (IOException e) {
                model.addAttribute("error", "Hubo un error al procesar la foto.");
                return "crear_mascota";
            }
        }

        nuevaMascota.setFechaInscripcion(LocalDateTime.now());

        mascotaRepository.save(nuevaMascota);

        model.addAttribute("mascota", nuevaMascota);
        return "redirect:/perfil";
    }


    @GetMapping("/usuario/editar")
    public String mostrarFormularioEdicionUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            model.addAttribute("errorMessage", "Debes iniciar sesión para editar tu perfil.");
            return "login";
        }

        model.addAttribute("usuario", usuario);
        return "editar_usuario";
    }


    @PostMapping("/usuario/actualizar")
    public String actualizarUsuario(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("correo") String correo,
            @RequestParam("username") String username,
            @RequestParam(value = "contraseña", required = false) String contraseña,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            model.addAttribute("errorMessage", "Debes iniciar sesión para editar tu perfil.");
            return "login";
        }

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuario.setUsername(username);

        if (contraseña != null && !contraseña.isEmpty()) {
            usuario.setContraseña(contraseña);
        }

        usuario.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);

        if (foto != null && !foto.isEmpty()) {
            try {
                File archivo = new File(foto.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(foto.getBytes());
                fos.close();

                String imagenUrl = cloudinaryService.subirImagen(archivo);

                usuario.setFoto(imagenUrl);

            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error al procesar la foto.");
                return "editar_usuario";
            }
        }

        usuarioService.actualizarUsuario(usuario.getUsuarioId(), usuario);

        session.setAttribute("usuario", usuario);

        return "redirect:/perfil";
    }


}

