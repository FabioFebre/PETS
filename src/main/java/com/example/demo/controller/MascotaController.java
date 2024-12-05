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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Optional;


@Controller
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UsuarioService usuarioService;
    private final DetalleVentaService detalleVentaService;

    private final CloudinaryService cloudinaryService;


    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaController(PasswordEncoder passwordEncoder, UsuarioService usuarioService, DetalleVentaService detalleVentaService, CloudinaryService cloudinaryService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
        this.detalleVentaService = detalleVentaService;
        this.cloudinaryService = cloudinaryService;
    }
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();

            String imagenUrl = usuario.getFoto();
            if (imagenUrl == null || imagenUrl.isEmpty()) {
                imagenUrl = "https://res.cloudinary.com/dq2suwtlm/image/upload/v1732582308/user.webp";
            } else {
                imagenUrl = limpiarRutaImagen(imagenUrl);
            }

            List<Mascota> mascotas = mascotaService.obtenerMascotasPorUsuario(usuario.getUsuarioId());
            for (Mascota mascota : mascotas) {
                String fotom = mascota.getFotom();
                if (fotom == null || fotom.isEmpty()) {
                    mascota.setFotom("https://res.cloudinary.com/dq2suwtlm/image/upload/v1732582308/default_mascota.webp");
                } else {
                    mascota.setFotom(limpiarRutaImagen(fotom));
                }
            }

            // Pasar los datos al modelo
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

        if (!username.equals(usuario.getUsername())) {
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorUsername(username);
            if (usuarioExistente != null) {
                model.addAttribute("errorMessage", "El nombre de usuario ya está en uso.");
                return "editar_usuario";
            }
        }

        try {
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            usuario.setUsername(username);

            // Si la contraseña fue modificada, la encriptamos
            if (contraseña != null && !contraseña.isEmpty()) {
                String contraseñaEncriptada = passwordEncoder.encode(contraseña);
                usuario.setContraseña(contraseñaEncriptada);
            }

            usuario.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);

            if (foto != null && !foto.isEmpty()) {
                File archivo = new File(foto.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(archivo)) {
                    fos.write(foto.getBytes());
                }
                String imagenUrl = cloudinaryService.subirImagen(archivo);
                usuario.setFoto(imagenUrl);
            }

            usuarioService.actualizarUsuario(usuario.getUsuarioId(), usuario);
            session.setAttribute("usuario", usuario);
            return "redirect:/perfil";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al actualizar el perfil: " + e.getMessage());
            return "editar_usuario";
        }
    }

    public String limpiarRutaImagen(String url) {
        if (url != null && url.contains("image/upload/")) {
            return url.replace("image/upload/", "");
        }
        return url;
    }


    @DeleteMapping("/mascota/eliminar/{mascotaId}")
    public String eliminarMascota(@PathVariable Integer mascotaId, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            try {
                Optional<Mascota> mascota = mascotaService.obtenerMascotaPorId(mascotaId);
                if (mascota.isPresent() && mascota.get().getUsuarioId().equals(usuario.getUsuarioId())) {
                    mascotaService.eliminarMascota(mascotaId);
                    model.addAttribute("successMessage", "Mascota eliminada exitosamente.");
                } else {
                    model.addAttribute("errorMessage", "Mascota no encontrada o no pertenece a tu cuenta.");
                }
                return "redirect:/perfil";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "redirect:/perfil";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para eliminar una mascota.");
            return "login";
        }
    }


    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}

