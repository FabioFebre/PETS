package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final ServicioService servicioService;
    private final VeterinarioService veterinarioService;

    public HomeController(ProductoService productoService,
                          UsuarioService usuarioService,
                          ServicioService servicioService,
                          CarritoService carritoService, VeterinarioService veterinarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.servicioService = servicioService;
        this.veterinarioService = veterinarioService;

    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            List<Servicio> servicios = servicioService.obtenerTodosLosServicios();
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            List<Veterinario> veterinarios = veterinarioService.getAllVeterinarios();

            model.addAttribute("usuario", usuario);
            model.addAttribute("servicios", servicios);
            model.addAttribute("productos", productos);
            model.addAttribute("veterinarios", veterinarios);


            return "index";
        }

        return "login";
    }
}
