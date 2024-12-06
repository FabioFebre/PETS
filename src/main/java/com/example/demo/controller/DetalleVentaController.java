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
            String rutaImagen = "http://localhost:8081/images/LOGO-PETS0.png"; // Ruta accesible

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><style>")
                    .append("body { font-family: Arial, sans-serif; padding: 20px; }")
                    .append("header { text-align: center; margin-bottom: 20px; }")
                    .append("header img { max-width: 100px; }")
                    .append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }")
                    .append("th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }")
                    .append("th { background-color: #f4f4f4; }")
                    .append(".total { font-size: 18px; font-weight: bold; margin-top: 20px; text-align: right; }")
                    .append("</style></head><body>");

            htmlContent.append("<header>")
                    .append("<img src='").append(rutaImagen).append("' alt='Logo Empresa'/>")
                    .append("<h2>Recibo de Compra</h2>")
                    .append("</header>");

            htmlContent.append("<p>Fecha de la Venta: ").append(venta.getFechaCompra()).append("</p>");
            htmlContent.append("<table><thead><tr><th>Producto</th><th>Cantidad</th><th>Precio</th><th>Total</th></tr></thead><tbody>");
            for (DetalleVenta detalle : venta.getDetalles()) {
                htmlContent.append("<tr>")
                        .append("<td>").append(detalle.getProducto().getNombre()).append("</td>")
                        .append("<td>").append(detalle.getCantidad()).append("</td>")
                        .append("<td>").append(detalle.getPrecio()).append("</td>")
                        .append("<td>").append(detalle.getTotal()).append("</td>")
                        .append("</tr>");
            }
            htmlContent.append("</tbody></table>");
            htmlContent.append("<p class='total'>Total: ").append(venta.getTotal()).append("</p>");
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
