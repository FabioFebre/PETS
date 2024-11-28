package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.repository.DetalleCarritoRepository;
import com.example.demo.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VentaController {

    private final UsuarioService usuarioService;
    private final MetodoPagoService metodoPagoService;
    @Autowired
    private ProductoCarritoService productoCarritoService;
    private final CarritoService carritoService;
    private final VentaService ventaService;
    private final DetalleVentaService detalleVentaService;
    @Autowired
    private ProductoService productoService;


    @Autowired
    public VentaController(UsuarioService usuarioService, MetodoPagoService metodoPagoService,
                           CarritoService carritoService, DetalleCarritoService detalleCarritoService,
                           VentaService ventaService, ProductoCarritoService productoCarritoService, DetalleVentaService detalleVentaService) {
        this.usuarioService = usuarioService;
        this.metodoPagoService = metodoPagoService;
        this.carritoService = carritoService;
        this.ventaService = ventaService;
        this.productoCarritoService = productoCarritoService;
        this.detalleVentaService = detalleVentaService;

    }

    @GetMapping("/pago")
    public String mostrarTransaccion(HttpSession session, Model model) {
        // Obtener usuario de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            // Obtener carrito, productos y calcular subtotal y total
            Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);
            List<ProductoCarrito> productosCarrito = productoCarritoService.obtenerProductosCarrito(carrito.getId(), usuario.getUsuarioId());
            List<Producto> productos = productoService.obtenerTodosLosProductos();

            BigDecimal subtotal = BigDecimal.ZERO;
            for (ProductoCarrito productoCarrito : productosCarrito) {
                BigDecimal precioUnitario = productoCarrito.getProducto().getPrecio();
                BigDecimal cantidad = BigDecimal.valueOf(productoCarrito.getCantidad());
                subtotal = subtotal.add(precioUnitario.multiply(cantidad));
            }


            BigDecimal igv = subtotal.multiply(BigDecimal.valueOf(0.10));
            BigDecimal total = subtotal.add(igv);

            // Obtener métodos de pago
            List<MetodoPago> metodosPago = metodoPagoService.obtenerTodosLosMetodos();

            // Agregar los atributos al modelo
            model.addAttribute("total", total);
            model.addAttribute("productos", productos);
            model.addAttribute("usuario", usuario);
            model.addAttribute("carrito", carrito);
            model.addAttribute("productosCarrito", productosCarrito);
            model.addAttribute("carritoId", carrito.getId());
            model.addAttribute("usuarioId", usuario.getUsuarioId());
            model.addAttribute("subtotal", subtotal);
            session.setAttribute("usuario", usuario);
            model.addAttribute("metodosPago", metodosPago);

            return "transaccion"; // Retorna la vista
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al carrito.");
            return "login";
        }
    }

    @PostMapping("/nuevaTransaccion")
    public String procesarPago(@RequestParam BigDecimal subtotal,
                               @RequestParam BigDecimal total,
                               @RequestParam("metodoPagoId") Long metodoPagoId,
                               @RequestParam(value = "numeroTarjeta", required = false) String numeroTarjeta,
                               @RequestParam(value = "correo", required = false) String correo,
                               @RequestParam(value = "fechaExpiracion", required = false) LocalDate fechaExpiracion,
                               @RequestParam(value = "cvv", required = false) String cvv,
                               HttpSession session, Model model) {
        // Obtener usuario de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            model.addAttribute("errorMessage", "Debes iniciar sesión para realizar una compra.");
            return "login";
        }

        // Obtener carrito del usuario
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);
        if (carrito == null || carrito.getProductoCarrito().isEmpty()) {
            model.addAttribute("errorMessage", "Tu carrito está vacío.");
            return "carrito";
        }

        // Verificar método de pago
        MetodoPago metodoPago = metodoPagoService.obtenerMetodoPorId(metodoPagoId);
        if (metodoPago == null) {
            model.addAttribute("errorMessage", "Método de pago no válido.");
            return "transaccion";
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setMetodo_id(metodoPago);
        venta.setCarrito(carrito);
        venta.setSubtotal(subtotal);
        venta.setTotal(total);

        if ("PayPal".equalsIgnoreCase(metodoPago.getNombre())) {
            if (correo == null || correo.isEmpty()) {
                model.addAttribute("errorMessage", "El correo de PayPal es obligatorio.");
                return "transaccion";
            }
            venta.setCorreo(correo);
        } else {
            // Manejar pagos con tarjeta
            if ( fechaExpiracion == null || cvv == null) {
                model.addAttribute("errorMessage", "Todos los campos de la tarjeta son obligatorios.");
                return "transaccion";
            }
            venta.setNumeroTarjeta(numeroTarjeta);
            venta.setFechaExpiracion(fechaExpiracion);
            venta.setCvv(cvv);
        }

        // Guardar venta y detalles
        ventaService.guardarVenta(venta);
        List<DetalleVenta> detallesVenta = new ArrayList<>();
        for (ProductoCarrito productoCarrito : carrito.getProductoCarrito()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(productoCarrito.getProducto());
            detalle.setCantidad(productoCarrito.getCantidad());
            detalle.setPrecio(productoCarrito.getProducto().getPrecio());
            detalle.setVenta(venta);
            detallesVenta.add(detalle);
        }
        detalleVentaService.guardarDetallesVenta(carrito, detallesVenta);

        // Limpiar el carrito
        productoCarritoService.eliminarProductosDelCarritoId(carrito.getId());

        // Mostrar mensaje de éxito
        model.addAttribute("mensajeExito", "Compra realizada exitosamente.");
        return "redirect:/carrito";
    }




}
