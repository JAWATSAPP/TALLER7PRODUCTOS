/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller7prod.Controller;

import com.example.taller7prod.model.Producto;
import org.springframework.ui.Model;
import com.example.taller7prod.service.ProductoService;
import com.itextpdf.kernel.colors.ColorConstants;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// pdf
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

// importaciones excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



/**
 *
 * @author sise
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService productoService) {
        this.service = productoService;
    }

    @GetMapping
    public String ListarProductos(Model model) {
        model.addAttribute("productos", this.service.ListarTodas());
        return "productos";
    }
    
    
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario";
    }

    @PostMapping
    public String guardarProductos(@ModelAttribute Producto producto) {
        this.service.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", this.service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID invalido" + id)));
        return "formulario";
    }
    


    @GetMapping("/eliminar/{id}")
    public String eliminarProductos(@PathVariable Long id) {
        this.service.eliminar(id);
        return "redirect:/productos";
    }

    @DeleteMapping("/{id}")
    public String eliminarPersonas(@PathVariable Long id) {
        this.service.eliminar(id);
        return "redirect:/productos";
    }
    
    
    

    @GetMapping("/reporte/pdf")
    public void generarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=productos_reporte.pdf");

        PdfWriter write = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(write));

// Título del reporte
        document.add(new Paragraph("Reporte de Productos").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

        // Ajustes de márgenes y tamaño de fuente 
        document.setMargins(20, 20, 20, 20);
        document.setFontSize(9);

        // Tabla con 10 columnas
        Table table = new Table(4);

        table.setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(20).setBorder(new SolidBorder(ColorConstants.BLACK, 1));

        table.addHeaderCell(new Paragraph("ID").setBold());
        table.addHeaderCell(new Paragraph("Nombre").setBold());
        table.addHeaderCell(new Paragraph("Descripcion").setBold());
        table.addHeaderCell(new Paragraph("Precio").setBold());

        // Obtener los empleados y añadir sus datos
        List<Producto> productos = this.service.ListarTodas();
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Instanciamos el formateador

        for (Producto producto : productos) {
            table.addCell(producto.getId().toString());
            table.addCell(producto.getNombre());
            table.addCell(producto.getDescripcion());
            table.addCell(producto.getPrecio().toString());

        }

        document.add(table);
        document.close();
    }
    @GetMapping("/reporte/excel")
    public void generarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos_reporte.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Nombre", "Descripcion", "Precio"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        List<Producto> productos = this.service.ListarTodas();
        int rowIndex = 1;
        for (Producto producto : productos) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getNombre());
            row.createCell(2).setCellValue(producto.getDescripcion());
            row.createCell(3).setCellValue(producto.getPrecio());
        
        }

        /*for (int i = 0; columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }*/
        workbook.write(response.getOutputStream());
        workbook.close();

    }

}


