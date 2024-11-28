package com.example.demo.controller;

import com.example.demo.models.Carrito;
import com.example.demo.models.Usuario;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CarritoRepository carritoRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/index")
    public String mostrarIndex(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "index";
        }
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session, Model model) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null && passwordEncoder.matches(password, usuario.getContraseña())) {
            session.setAttribute("usuario", usuario);

            Carrito carrito = carritoRepository.findByUsuario_UsuarioId(usuario.getUsuarioId());
            if (carrito == null) {
                carrito = new Carrito();
                carrito.setUsuario(usuario);
                carritoRepository.save(carrito);
            }

            return "redirect:/";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam("nombres") String nombres,
                                   @RequestParam("apellidos") String apellidos,
                                   @RequestParam("correo") String correo,
                                   @RequestParam("username") String username,
                                   @RequestParam("contraseña") String contraseña,
                                   @RequestParam("fechaNacimiento") String fechaNacimiento,
                                   @RequestParam("telefono") String telefono,
                                   @RequestParam("direccion") String direccion,
                                   HttpSession session, Model model) {
        Usuario usuarioExistente = usuarioRepository.findByUsername(username);
        if (usuarioExistente != null) {
            model.addAttribute("error", "El nombre de usuario ya existe.");
            return "registro";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(nombres);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setUsername(username);

        String contraseñaEncriptada = passwordEncoder.encode(contraseña);
        nuevoUsuario.setContraseña(contraseñaEncriptada);

        if (!fechaNacimiento.isEmpty()) {
            nuevoUsuario.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
        }

        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setDireccion(direccion);
        nuevoUsuario.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(nuevoUsuario);

        return "redirect:/login";
    }



}
