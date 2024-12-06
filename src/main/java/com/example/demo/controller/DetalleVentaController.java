package com.example.demo.controller;



import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Usuario;
import com.example.demo.models.Venta;
import com.example.demo.services.DetalleVentaService;
import com.example.demo.services.PdfGenerationService;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;
    @Autowired
    private PdfGenerationService pdfGenerationService;
    @Autowired
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }


    @GetMapping("/perfil/detalles-venta")
    public String listarDetallesVenta(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Venta> ventasConDetalles = detalleVentaService.listarDetallesPorUsuario(usuario);

            if (!ventasConDetalles.isEmpty()) {
                model.addAttribute("ventas", ventasConDetalles);
                model.addAttribute("usuario", usuario);

                return "detalles-venta";
            } else {
                model.addAttribute("errorMessage", "No tienes ventas registradas.");
                return "detalles-venta";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para ver tus detalles de venta.");
            return "login";
        }
    }

    @GetMapping("/perfil/detalles-venta/pdf/{id}")
    public void generarPdfVenta(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            Venta venta = detalleVentaService.obtenerVentaPorId(id);

            String rutaImagen = "/images/LOGO-PETS0.png";

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><style>")
                    .append("img { position: absolute; top: 10px; right: 10px; width: 100px; }") // Estilo para la imagen
                    .append(".detalles-container { display: flex; flex-wrap: wrap; gap: 20px; padding: 20px; }")
                    .append(".detalle-venta { border: 1px solid #ccc; padding: 15px; width: 280px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }")
                    .append(".detalle-venta h3 { margin-top: 0; font-size: 18px; }")
                    .append(".detalle-venta p { margin: 5px 0; }")
                    .append(".detalle-venta .total { font-weight: bold; color: green; }")
                    .append(".detalle-venta .fecha-compra { color: #777; }")
                    .append("</style></head><body>");

            htmlContent.append("<img src='" + rutaImagen + "' alt='Logo Empresa'/>");

            htmlContent.append("<h2>Detalles de Venta - Venta ID: " + venta.getId() + "</h2>");
            htmlContent.append("<p>Fecha de la Venta: " + venta.getFechaCompra() + "</p>");

            htmlContent.append("<table><thead><tr><th>Producto</th><th>Cantidad</th><th>Precio</th><th>Total</th></tr></thead><tbody>");

            for (DetalleVenta detalle : venta.getDetalles()) {
                htmlContent.append("<tr>");
                htmlContent.append("<td>" + detalle.getProducto().getNombre() + "</td>");
                htmlContent.append("<td>" + detalle.getCantidad() + "</td>");
                htmlContent.append("<td>" + detalle.getPrecio() + "</td>");
                htmlContent.append("<td>" + detalle.getTotal() + "</td>");
                htmlContent.append("</tr>");
            }

            htmlContent.append("</tbody></table>");

            htmlContent.append("<p class='total'>Total: " + venta.getTotal() + "</p>");

            htmlContent.append("</body></html>");

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=venta_" + venta.getId() + ".pdf");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();

            renderer.createPDF(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
