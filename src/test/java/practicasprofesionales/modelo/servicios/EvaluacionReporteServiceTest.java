/*
 * Pruebas unitarias para EvaluacionReporteService utilizando Mockito.
 */
package practicasprofesionales.modelo.servicios;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.DocumentoDAO;
import practicasprofesionales.modelo.pojo.ReporteEstudiante;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

import java.util.Arrays;
import java.util.List;

public class EvaluacionReporteServiceTest {

    @Mock
    private DocumentoDAO documentoDAOMock;

    @InjectMocks
    private EvaluacionReporteService service;

    private ReporteEstudiante reportePrueba;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reportePrueba = new ReporteEstudiante();
        reportePrueba.setIdDocumento(10);
    }

    @Test
    public void testObtenerReportesPorTipoValido() throws ExcepcionDAO {
        when(documentoDAOMock.obtenerIdCatalogoDocumento("Reporte Mensual 1")).thenReturn(2);
        when(documentoDAOMock.obtenerReportesPorCatalogo(2)).thenReturn(Arrays.asList(reportePrueba));

        List<ReporteEstudiante> resultado = service.obtenerReportesPorTipo("Reporte Mensual 1");
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getIdDocumento());
    }

    @Test
    public void testObtenerReportesPorTipoInvalido() throws ExcepcionDAO {
        when(documentoDAOMock.obtenerIdCatalogoDocumento("Tipo Inexistente")).thenReturn(-1);

        List<ReporteEstudiante> resultado = service.obtenerReportesPorTipo("Tipo Inexistente");
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testAsignarCalificacionVacia() {
        RespuestaOperacion respuesta = service.asignarCalificacion(reportePrueba, "", "Buen trabajo");
        assertTrue(respuesta.isError());
        assertEquals("Valor inválido. Ingrese una calificación válida.", respuesta.getMensaje());
    }

    @Test
    public void testAsignarCalificacionFueraDeRango() {
        RespuestaOperacion respuesta = service.asignarCalificacion(reportePrueba, "11", "Buen trabajo");
        assertTrue(respuesta.isError());
        assertEquals("Valor inválido. Ingrese una calificación válida entre 0 y 10.", respuesta.getMensaje());
    }

    @Test
    public void testAsignarCalificacionNoNumerica() {
        RespuestaOperacion respuesta = service.asignarCalificacion(reportePrueba, "Diez", "Buen trabajo");
        assertTrue(respuesta.isError());
        assertEquals("Valor numérico inválido. Ingrese una calificación válida.", respuesta.getMensaje());
    }

    @Test
    public void testAsignarCalificacionExitosa() throws ExcepcionDAO {
        when(documentoDAOMock.actualizarCalificacionYObservacion(10, "9.5", "Excelente")).thenReturn(true);

        RespuestaOperacion respuesta = service.asignarCalificacion(reportePrueba, "9.5", "Excelente");
        assertFalse(respuesta.isError());
        assertEquals("Reporte evaluado exitosamente", respuesta.getMensaje());
        assertEquals("9.5", reportePrueba.getCalificacion());
        assertEquals("Excelente", reportePrueba.getObservacion());
        
        verify(documentoDAOMock, times(1)).actualizarCalificacionYObservacion(10, "9.5", "Excelente");
    }
}
