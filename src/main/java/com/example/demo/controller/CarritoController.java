package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.repository.ProductoCarritoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.services.CarritoService;
import com.example.demo.services.DetalleCarritoService;
import com.example.demo.services.ProductoService;
import com.example.demo.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;
    @Autowired
    private DetalleCarritoService detalleCarritoService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            List<Producto> productosDisponibles = productoService.obtenerTodosLosProductos();

            model.addAttribute("usuario", usuario);
            model.addAttribute("carrito", carritoService.obtenerContenidoCarrito(usuario));
            model.addAttribute("productos", productosDisponibles);
            return "carrito";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al carrito.");
            return "login";
        }
    }


    @PostMapping("/agregarProducto")
    public String agregarProducto(
            @RequestParam("productoId") Long productoId,
            @RequestParam("carritoId") Long carritoId,
            @RequestParam("cantidad") int cantidad,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            model.addAttribute("error", "Debes iniciar sesión para agregar productos al carrito.");
            return "login";
        }

        // Buscar el producto
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (!productoOpt.isPresent()) {
            model.addAttribute("error", "Producto no encontrado.");
            return "redirect:/carrito";
        }
        Producto producto = productoOpt.get();

        // Buscar el carrito
        Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
        if (!carritoOpt.isPresent()) {
            model.addAttribute("error", "Carrito no encontrado.");
            return "redirect:/carrito";
        }
        Carrito carrito = carritoOpt.get();

        // Crear o actualizar el ProductoCarrito
        ProductoCarrito productoCarrito = productoCarritoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(new ProductoCarrito(carrito, producto, 0));

        productoCarrito.setCantidad(productoCarrito.getCantidad() + cantidad);
        productoCarrito.setPrecio(producto.getPrecio().multiply(BigDecimal.valueOf(productoCarrito.getCantidad())));

        productoCarritoRepository.save(productoCarrito);

        model.addAttribute("mensaje", "Producto agregado al carrito correctamente.");
        return "redirect:/carrito";
    }





    @PostMapping("/eliminarProductoCarrito")
    public String eliminarProductoDelCarrito(@RequestParam("productoId") Long productoId,
                                             @RequestParam("carritoId") Long carritoId,
                                             Model model) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        List<ProductoCarrito> productosCarrito = productoCarritoRepository.findByCarrito(carrito);

        ProductoCarrito productoCarritoAEliminar = null;
        for (ProductoCarrito pc : productosCarrito) {
            if (pc.getProducto().equals(producto)) {
                productoCarritoAEliminar = pc;
                break;
            }
        }

        if (productoCarritoAEliminar != null) {
            productoCarritoRepository.delete(productoCarritoAEliminar);
            model.addAttribute("mensaje", "Producto eliminado del carrito exitosamente.");
        } else {
            model.addAttribute("error", "El producto no se encontró en el carrito.");
        }

        return "redirect:/carrito";
    }



}
