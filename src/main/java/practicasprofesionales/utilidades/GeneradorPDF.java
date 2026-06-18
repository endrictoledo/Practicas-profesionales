/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.utilidades;

import practicasprofesionales.modelo.pojo.SolicitudEstudiante;
import practicasprofesionales.modelo.pojo.SolicitudPractica;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author basa2
 */


public class GeneradorPDF {

    public static void generarCartaAceptacion(SolicitudPractica sol, 
                                SolicitudEstudiante proy, LocalDate inicio,
                                LocalDate fin, File destino) throws Exception {
        PdfWriter writer = new PdfWriter(destino.getAbsolutePath());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                                      "dd 'de' MMMM 'de' yyyy");
        document.add(new Paragraph("Xalapa-Enríquez, Ver., a " + 
                LocalDate.now().format(formatter)).setTextAlignment(
                                                          TextAlignment.RIGHT));
        document.add(new Paragraph("\nDR. ÁNGEL JUAN SÁNCHEZ GARCÍA\n" +
                                   "COORDINADOR DE PRÁCTICAS PROFESIONALES\n" +
                                   "LICENCIATURA EN INGENIERÍA DE SOFTWARE\n" +
                                   "FACULTAD DE ESTADÍSTICA E INFORMÁTICA\n" +
                                   "UNIVERSIDAD VERACRUZANA\n" +
                                   "P R E S E N T E")
                                   .setMarginTop(20));
        String cuerpo = String.format(
                "\nPor medio de la presente le informo que el C. %s, " 
                + "alumno de la Facultad de Estadística e Informática"
                + " con matrícula %s, " 
                + "ha sido aceptado para realizar sus prácticas profesionales"
                + " en %s, " 
                + "teniendo como fecha de inicio %s y aproximada de "
                + "terminación %s, " 
                + "en el cual cubrirá un total de 200 horas, en las que"
                + " realizará actividades afines a su carrera.",
                sol.getNombreEstudiante(), sol.getMatricula(), 
                proy.getProyecto().getNombreEmpresa(), 
                inicio.format(formatter), fin.format(formatter));

        document.add(new Paragraph(cuerpo).setMarginTop(20));
        
        document.add(new Paragraph("\nSin más por el momento quedo a su"
                                    + " disposición para cualquier aclaración.")
                                    .setMarginTop(20));

        document.add(new Paragraph("\n\nAtentamente,"
                                 + "\n\n__________________________\n" +
                                 proy.getProyecto().getNombreResponsable())
                                .setTextAlignment(TextAlignment.CENTER));
        document.close();
    }
}