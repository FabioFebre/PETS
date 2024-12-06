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
                .append("body { font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9; }")
                .append("header { text-align: center; margin-bottom: 20px; }")
                .append("header img { max-width: 150px; margin-bottom: 10px; }")
                .append("header h1 { font-size: 24px; color: #333; margin: 0; }")
                .append(".receipt { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 0 auto; }")
                .append(".receipt h2 { font-size: 20px; margin-bottom: 10px; color: #555; }")
                .append(".receipt p { margin: 5px 0; font-size: 14px; color: #666; }")
                .append(".details-table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append(".details-table th, .details-table td { border: 1px solid #ddd; padding: 10px; text-align: left; }")
                .append(".details-table th { background-color: #f2f2f2; font-weight: bold; }")
                .append(".total-row { font-weight: bold; color: #333; }")
                .append("</style></head><body>")
                // Encabezado con el logo
                .append("<header>")
                .append("<img src='static/images/LOGO-PETS0.png' alt='Logo Pets'>")

                .append("<h1>Recibo de Compra</h1>")
                .append("</header>");

        html.append("<div class='receipt'>");

        if (detallesVenta != null && !detallesVenta.isEmpty()) {
            html.append("<h2>Detalles de Venta</h2>");
            html.append("<p>Fecha: ").append(detallesVenta.get(0).getVenta().getFechaCompra()).append("</p>");
            html.append("<table class='details-table'>")
                    .append("<thead>")
                    .append("<tr>")
                    .append("<th>Producto</th>")
                    .append("<th>Cantidad</th>")
                    .append("<th>Precio Unitario</th>")
                    .append("<th>Total</th>")
                    .append("</tr>")
                    .append("</thead><tbody>");


            }

        html.append("</div></body></html>");
        return html.toString();
    }

}
