package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private ProductoCarritoService productoCarritoService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DetalleCarritoService detalleCarritoService;

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);
            List<ProductoCarrito> productosCarrito = productoCarritoService.obtenerProductosCarrito(carrito.getId(), usuario.getUsuarioId());
            List<Producto> productos = productoService.obtenerTodosLosProductos();

            model.addAttribute("usuario", usuario);
            model.addAttribute("carrito", carrito);
            model.addAttribute("productos", productos);
            model.addAttribute("productosCarrito", productosCarrito);
            model.addAttribute("carritoId", carrito.getId());
            model.addAttribute("usuarioId", usuario.getUsuarioId());

            return "carrito";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para acceder al carrito.");
            return "login";
        }
    }

    @PostMapping("/carrito/agregar")
    public String agregarProductoAlCarrito(@RequestParam("producto_id") Long producto_id,
                                           @RequestParam("cantidad") Integer cantidad,
                                           HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);

            Producto producto = productoRepository.findById(producto_id).orElse(null);

            if (producto != null && carrito != null) {
                ProductoCarrito productoCarrito = new ProductoCarrito();
                productoCarrito.setCarrito(carrito);
                productoCarrito.setProducto(producto);
                productoCarrito.setCantidad(cantidad);

                BigDecimal precioUnitario = producto.getPrecio();
                BigDecimal cantidadDecimal = new BigDecimal(cantidad);
                BigDecimal precioTotal = precioUnitario.multiply(cantidadDecimal);
                productoCarrito.setPrecio(precioTotal);

                productoCarritoService.agregarProductoAlCarrito(productoCarrito);

                DetalleCarrito detalleCarrito = new DetalleCarrito();
                detalleCarrito.setCarrito(carrito);
                detalleCarrito.setProducto(producto);
                detalleCarrito.setCantidad(cantidad);
                detalleCarrito.setPrecio(precioTotal);


                List<ProductoCarrito> productosCarrito = productoCarritoService.obtenerProductosCarrito(carrito.getId(), usuario.getUsuarioId());


                model.addAttribute("usuario", usuario);
                model.addAttribute("carrito", carrito);
                model.addAttribute("productosCarrito", productosCarrito);
                model.addAttribute("carritoId", carrito.getId());
                model.addAttribute("usuarioId", usuario.getUsuarioId());

                return "redirect:/carrito";
            } else {
                model.addAttribute("errorMessage", "Producto o carrito no válido.");
                return "carrito";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para agregar productos al carrito.");
            return "login";
        }
    }


    @PostMapping("/carrito/eliminar")
    public String eliminarProductoDelCarrito(@RequestParam("producto_id") Long producto_id,
                                             HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);

            if (carrito != null) {
                productoCarritoService.eliminarProductoDelCarrito(carrito, producto_id);

                List<ProductoCarrito> productosCarrito = productoCarritoService.obtenerProductosCarrito(carrito.getId(), usuario.getUsuarioId());

                model.addAttribute("usuario", usuario);
                model.addAttribute("carrito", carrito);
                model.addAttribute("productosCarrito", productosCarrito);
                model.addAttribute("carritoId", carrito.getId());
                model.addAttribute("usuarioId", usuario.getUsuarioId());

                return "redirect:/carrito";
            } else {
                model.addAttribute("errorMessage", "Carrito no encontrado.");
                return "carrito";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para eliminar productos del carrito.");
            return "login";
        }
    }


}
