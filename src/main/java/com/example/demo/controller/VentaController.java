package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.repository.ProductoCarritoRepository;
import com.example.demo.repository.VentaRepository;
import com.example.demo.services.*;
import jakarta.servlet.http.HttpSession;
import com.example.demo.models.DetalleCarrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class VentaController {

    @Autowired
    private UsuarioService usuarioService;
    private final MetodoPagoService metodoPagoService;
    private final ProductoCarritoService productoCarritoService;

    @Autowired
    private VentaRepository ventaRepository;
    private final CarritoService carritoService;
    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;
    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;
    @Autowired
    private DetalleCarritoService detalleCarritoService;
    private final VentaService ventaService;

    @Autowired


    public VentaController(MetodoPagoService metodoPagoService, ProductoCarritoService productoCarritoService, CarritoService carritoService, VentaService ventaService) {
        this.metodoPagoService = metodoPagoService;
        this.productoCarritoService = productoCarritoService;
        this.carritoService = carritoService;
        this.ventaService = ventaService;
    }


    @GetMapping("/pago")
    public String mostrarTransaccion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Carrito carrito = carritoService.obtenerContenidoCarrito(usuario);

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal subtotal = BigDecimal.ZERO;
            int totalProductos = 0;

            // Comprobar si el carrito no es nulo y tiene productos
            if (carrito != null && carrito.getProductoCarrito() != null) {
                totalProductos = carrito.getProductoCarrito().stream()
                        .mapToInt(ProductoCarrito::getCantidad)
                        .sum();

                subtotal = carrito.getProductoCarrito().stream()
                        .map(productoCarrito -> productoCarrito.getPrecio()
                                .multiply(new BigDecimal(productoCarrito.getCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("detalles", carrito.getProductoCarrito());
            model.addAttribute("carrito", carrito);
            return "transaccion";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al carrito.");
            return "login";
        }
    }




    @PostMapping("/nuevaTransaccion")
    public String registrarTransaccion(@RequestParam("metodoPagoId") Long metodoPagoId,
                                       @RequestParam("numeroTarjeta") String numeroTarjeta,
                                       @RequestParam("correo") String correo,
                                       @RequestParam("fechaExpiracion") String fechaExpiracion,
                                       @RequestParam("cvv") String cvv,
                                       @RequestParam("usuarioId") Integer usuarioId,
                                       @RequestParam("carritoId") Long carritoId,
                                       @RequestParam("subtotal") BigDecimal subtotal,
                                       Model model) {
        Venta nuevaTransaccion = new Venta();

        MetodoPago metodoPago = metodoPagoService.obtenerMetodoPorId(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        nuevaTransaccion.setMetodoPago(metodoPago);

        nuevaTransaccion.setNumeroTarjeta(numeroTarjeta);
        nuevaTransaccion.setCorreo(correo);
        nuevaTransaccion.setFechaExpiracion(LocalDate.parse(fechaExpiracion));
        nuevaTransaccion.setCvv(cvv);
        nuevaTransaccion.setSubtotal(subtotal);

        BigDecimal total = subtotal.multiply(BigDecimal.valueOf(1.10));
        nuevaTransaccion.setTotal(total);

        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        Carrito carrito = carritoService.obtenerCarritoPorId(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        nuevaTransaccion.setCarrito(carrito);
        nuevaTransaccion.setUsuario(usuario);

        ventaRepository.save(nuevaTransaccion);

        productoCarritoService.eliminarProductosDelCarrito(carrito.getId());

        return "redirect:/carrito";
    }


}
