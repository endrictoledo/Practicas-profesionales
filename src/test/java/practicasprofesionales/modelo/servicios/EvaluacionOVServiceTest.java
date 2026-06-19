/*
 * Pruebas unitarias para EvaluacionOVService utilizando Mockito.
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
import practicasprofesionales.modelo.pojo.Documento;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

public class EvaluacionOVServiceTest {

    @Mock
    private DocumentoDAO documentoDAOMock;

    @InjectMocks
    private EvaluacionOVService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCamposIncompletos() {

        RespuestaOperacion respuesta = service.procesarEvaluacion(1, "", "Alta", "Sí", "Ninguna");
        assertTrue(respuesta.isError());
        assertEquals("Por favor, responda todas las preguntas para continuar.", respuesta.getMensaje());
    }

    @Test
    public void testEstudianteSinExpediente() throws ExcepcionDAO {
        when(documentoDAOMock.obtenerIdExpedientePorUsuario(1)).thenReturn(-1);

        RespuestaOperacion respuesta = service.procesarEvaluacion(1, "10", "Alta", "Sí", "Ninguna");
        assertTrue(respuesta.isError());
        assertEquals("El estudiante no tiene un expediente asignado. No se puede guardar la evaluación.", respuesta.getMensaje());
    }

    @Test
    public void testCatalogoNoEncontrado() throws ExcepcionDAO {
        when(documentoDAOMock.obtenerIdExpedientePorUsuario(1)).thenReturn(100);
        when(documentoDAOMock.obtenerIdCatalogoDocumento("Evaluación OV")).thenReturn(-1);

        RespuestaOperacion respuesta = service.procesarEvaluacion(1, "10", "Alta", "Sí", "Ninguna");
        assertTrue(respuesta.isError());
        assertEquals("No se encontró el tipo de documento 'Evaluación OV' en la base de datos.", respuesta.getMensaje());
    }

    @Test
    public void testEvaluacionExitosa() throws ExcepcionDAO {
        when(documentoDAOMock.obtenerIdExpedientePorUsuario(1)).thenReturn(100);
        when(documentoDAOMock.obtenerIdCatalogoDocumento("Evaluación OV")).thenReturn(17);
        when(documentoDAOMock.registrarDocumento(any(Documento.class))).thenReturn(true);

        RespuestaOperacion respuesta = service.procesarEvaluacion(1, "10", "Alta", "Sí", "Ninguna");
        assertFalse(respuesta.isError());
        assertEquals("Has realizado la evaluación correctamente.", respuesta.getMensaje());

        verify(documentoDAOMock, times(1)).registrarDocumento(any(Documento.class));
    }
}
