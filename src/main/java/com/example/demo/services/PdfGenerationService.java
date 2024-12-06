package com.example.demo.services;

import com.example.demo.models.DetalleVenta;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfGenerationService {

    public void generarPdf(List<DetalleVenta> detallesVenta, String outputPath) throws Exception {
        String html = crearHtml(detallesVenta);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (FileOutputStream os = new FileOutputStream(outputPath)) {
            renderer.createPDF(os);
        }
    }
    private String crearHtml(List<DetalleVenta> detallesVenta) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; padding: 20px; }")
                .append("h1 { color: #333; }")
                .append(".detalles-container { display: flex; flex-wrap: wrap; gap: 20px; padding: 20px; }")
                .append(".detalle-venta { border: 1px solid #ccc; padding: 15px; width: 280px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }")
                .append(".detalle-venta h3 { margin-top: 0; font-size: 18px; }")
                .append(".detalle-venta p { margin: 5px 0; }")
                .append(".detalle-venta .total { font-weight: bold; color: green; }")
                .append(".detalle-venta .fecha-compra { color: #777; }")
                .append("</style></head><body>");

        html.append("<h1>Detalles de Venta</h1>");

        if (detallesVenta != null && !detallesVenta.isEmpty()) {
            html.append("<div class='detalles-container'>");
            for (DetalleVenta detalle : detallesVenta) {
                html.append("<div class='detalle-venta'>");
                html.append("<h3>Producto: ").append(detalle.getProducto().getNombre()).append("</h3>");
                html.append("<p>Cantidad: ").append(detalle.getCantidad()).append("</p>");
                html.append("<p>Precio: ").append(detalle.getPrecio()).append("</p>");
                html.append("<p>Total: ").append(detalle.getTotal()).append("</p>");
                html.append("<p>Numero de Tarjeta: ").append(detalle.getVenta().getNumeroTarjeta()).append("</p>");
                html.append("<p class='fecha-compra'>Fecha Compra: ").append(detalle.getVenta().getFechaCompra()).append("</p>");
                html.append("</div>");
            }
            html.append("</div>");
        } else {
            html.append("<p>No tienes detalles de venta disponibles.</p>");
        }

        html.append("</body></html>");
        return html.toString();
    }


}
