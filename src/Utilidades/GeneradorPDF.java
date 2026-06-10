/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import Modelo.*;
import Vista.IVista;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.List;

/**
 *
 * @author Braya
 */
public class GeneradorPDF {
    public static void generarFactura(Ventas venta, List<DetalleVentas> detalles, 
                                     String nombreCliente, String nombreUsuario, IVista vista) {
        
        String nombreArchivo = "Factura_" + venta.getIdVenta() + ".pdf";
        
        try {
            // ===== CREAR DOCUMENTO PDF =====
            Document documento = new Document(PageSize.LETTER);
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();
            
            // ===== DEFINIR FUENTES =====
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font fuenteTablaHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fuenteTotales = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            
            // ===== TÍTULO =====
            Paragraph titulo = new Paragraph("FACTURA DE VENTA", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);
            
          
            Paragraph infoEmpresa = new Paragraph();
            infoEmpresa.add(new Chunk("Tienda Electrónica S.A.\n", fuenteSubtitulo));
            infoEmpresa.add(new Chunk("Dirección: Av. Principal #123\n", fuenteNormal));
            infoEmpresa.add(new Chunk("Teléfono: (123) 456-7890\n", fuenteNormal));
            infoEmpresa.add(new Chunk("Email: ventas@tienda.com\n\n", fuenteNormal));
            documento.add(infoEmpresa);
            
            
            documento.add(new Paragraph("─────────────────────────────────────────────────────────────"));
            documento.add(Chunk.NEWLINE);
            
           
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{1, 1});
            
            // Columna izquierda
            PdfPCell celdaIzq = new PdfPCell();
            celdaIzq.setBorder(Rectangle.NO_BORDER);
            celdaIzq.addElement(new Paragraph("Factura No: " + venta.getIdVenta(), fuenteSubtitulo));
            celdaIzq.addElement(new Paragraph("Fecha: " + venta.getFechaVentas(), fuenteNormal));
            celdaIzq.addElement(new Paragraph("Estado: " + venta.getEstado(), fuenteNormal));
            
            // Columna derecha
            PdfPCell celdaDer = new PdfPCell();
            celdaDer.setBorder(Rectangle.NO_BORDER);
            celdaDer.addElement(new Paragraph("Cliente: " + nombreCliente, fuenteNormal));
            celdaDer.addElement(new Paragraph("Atendido por: " + nombreUsuario, fuenteNormal));
            
            tablaInfo.addCell(celdaIzq);
            tablaInfo.addCell(celdaDer);
            documento.add(tablaInfo);
            
            documento.add(Chunk.NEWLINE);
            documento.add(new Paragraph("─────────────────────────────────────────────────────────────"));
            documento.add(Chunk.NEWLINE);
            
            // ===== TABLA DE PRODUCTOS =====
            PdfPTable tablaProductos = new PdfPTable(5);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{3, 1, 1.5f, 1.5f, 1.5f});
            
            // Encabezados de la tabla
            String[] headers = {"Producto", "Cant.", "Precio Unit.", "IVA", "Subtotal"};
            for (String header : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(header, fuenteTablaHeader));
                celda.setBackgroundColor(BaseColor.DARK_GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(5);
                tablaProductos.addCell(celda);
            }
            
            // Obtener conexión a la base de datos
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM Productos WHERE id_producto = ?");
            
            double subtotalGeneral = 0;
            
            // Agregar productos a la tabla
            for (DetalleVentas detalle : detalles) {
                // Obtener nombre del producto
                ps.setInt(1, detalle.getIdProducto());
                ResultSet rs = ps.executeQuery();
                
                String nombreProducto = "";
                if (rs.next()) {
                    nombreProducto = rs.getString("nombre");
                }
                rs.close();
                
                // Calcular subtotal e IVA de esta línea
                double subtotalLinea = detalle.getCantidad() * detalle.getPrecioUnitario();
                double ivaLinea = subtotalLinea * 0.13;
                subtotalGeneral += subtotalLinea;
                
                // Agregar fila a la tabla
                tablaProductos.addCell(new PdfPCell(new Phrase(nombreProducto, fuenteNormal)));
                
                PdfPCell celdaCant = new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), fuenteNormal));
                celdaCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaProductos.addCell(celdaCant);
                
                PdfPCell celdaPrecio = new PdfPCell(new Phrase("$" + String.format("%.2f", detalle.getPrecioUnitario()), fuenteNormal));
                celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tablaProductos.addCell(celdaPrecio);
                
                PdfPCell celdaIVA = new PdfPCell(new Phrase("$" + String.format("%.2f", ivaLinea), fuenteNormal));
                celdaIVA.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tablaProductos.addCell(celdaIVA);
                
                PdfPCell celdaSubtotal = new PdfPCell(new Phrase("$" + String.format("%.2f", subtotalLinea), fuenteNormal));
                celdaSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tablaProductos.addCell(celdaSubtotal);
            }
            
            ps.close();
            documento.add(tablaProductos);
            documento.add(Chunk.NEWLINE);
            
            // ===== TABLA DE TOTALES =====
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(40);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setWidths(new float[]{1, 1});
            
            double ivaTotal = subtotalGeneral * 0.13;
            double totalFinal = subtotalGeneral + ivaTotal;
            
            // Subtotal
            tablaTotales.addCell(new PdfPCell(new Phrase("Subtotal:", fuenteNormal)));
            PdfPCell celdaSubtotal = new PdfPCell(new Phrase("$" + String.format("%.2f", subtotalGeneral), fuenteNormal));
            celdaSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.addCell(celdaSubtotal);
            
            // IVA
            tablaTotales.addCell(new PdfPCell(new Phrase("IVA (13%):", fuenteNormal)));
            PdfPCell celdaIVA = new PdfPCell(new Phrase("$" + String.format("%.2f", ivaTotal), fuenteNormal));
            celdaIVA.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.addCell(celdaIVA);
            
            // Total
            PdfPCell celdaTotalLabel = new PdfPCell(new Phrase("TOTAL:", fuenteTotales));
            celdaTotalLabel.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaTotales.addCell(celdaTotalLabel);
            
            PdfPCell celdaTotalValor = new PdfPCell(new Phrase("$" + String.format("%.2f", totalFinal), fuenteTotales));
            celdaTotalValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalValor.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaTotales.addCell(celdaTotalValor);
            
            documento.add(tablaTotales);
            
            // ===== OBSERVACIONES =====
            if (venta.getObservaciones() != null && !venta.getObservaciones().isEmpty()) {
                documento.add(Chunk.NEWLINE);
                documento.add(new Paragraph("Observaciones:", fuenteSubtitulo));
                documento.add(new Paragraph(venta.getObservaciones(), fuenteNormal));
            }
            
            // ===== PIE DE PÁGINA =====
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("¡Gracias por su compra!", fuenteSubtitulo);
            footer.setAlignment(Element.ALIGN_CENTER);
            documento.add(footer);
            
            // Cerrar documento
            documento.close();
            
            // Mostrar mensaje de éxito
            vista.mostrarMensaje("Factura PDF generada exitosamente: " + nombreArchivo, "Éxito");
            
        } catch (Exception e) {
            vista.mostrarError("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
