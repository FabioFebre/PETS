package com.example.demo.controller;

import com.example.demo.models.Cita;
import com.example.demo.models.Horario;
import com.example.demo.models.Mascota;
import com.example.demo.models.Servicio;
import com.example.demo.models.Usuario;
import com.example.demo.services.CitaService;
import com.example.demo.services.HorarioService;
import com.example.demo.services.MascotaService;
import com.example.demo.services.ServicioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CitaController {

    private final CitaService citaService;
    private final MascotaService mascotaService;
    private final ServicioService servicioService;
    private final HorarioService horarioService;

    @Autowired
    public CitaController(CitaService citaService, MascotaService mascotaService,
                          ServicioService servicioService, HorarioService horarioService) {
        this.citaService = citaService;
        this.mascotaService = mascotaService;
        this.servicioService = servicioService;
        this.horarioService = horarioService;
    }

    @GetMapping("/citas")
    public String listarCitas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {

            List<Mascota> mascotas = mascotaService.obtenerMascotasPorUsuario(usuario.getUsuarioId());
            List<Servicio> servicios = servicioService.obtenerTodosLosServicios();
            List<Horario> horarios = horarioService.obtenerTodosLosHorarios();
            List<Cita> citas = citaService.obtenerCitasPorUsuario(usuario);

            model.addAttribute("citas", citas);
            model.addAttribute("usuario", usuario);
            model.addAttribute("mascotas", mascotas);
            model.addAttribute("servicios", servicios);
            model.addAttribute("horarios", horarios);
            model.addAttribute("nuevaCita", new Cita());

            return "lista";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder a las citas.");
            return "login";
        }
    }

    @PostMapping("/citas")
    public String registrarCita(@ModelAttribute("nuevaCita") Cita nuevaCita, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            try {
                nuevaCita.setUsuario(usuario);
                nuevaCita.calcularCostoCita();
                nuevaCita.setEstado(false);
                citaService.createCita(nuevaCita);
                return "redirect:/citas";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "redirect:/citas";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para registrar una cita.");
            return "login";
        }
    }

    @DeleteMapping("/citas/eliminar/{citaId}")
    public String eliminarCita(@PathVariable Long citaId, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            try {
                citaService.eliminarCita(citaId);
                model.addAttribute("successMessage", "Cita eliminada exitosamente.");
                return "redirect:/citas";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "redirect:/citas";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para eliminar una cita.");
            return "login";
        }
    }

}
