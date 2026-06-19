/*
 * Pruebas unitarias para SubirDocumentoService utilizando Mockito.
 */
package practicasprofesionales.modelo.servicios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.DocumentoDAO;
import practicasprofesionales.modelo.pojo.Documento;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.SesionGlobal;

public class SubirDocumentoServiceTest {

    @Mock
    private DocumentoDAO documentoDAOMock;

    @InjectMocks
    private SubirDocumentoService service;

    private MockedStatic<SesionGlobal> sesionGlobalMockStatic;
    private SesionGlobal sesionGlobalMock;
    private File archivoValido;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        sesionGlobalMock = mock(SesionGlobal.class);
        sesionGlobalMockStatic = mockStatic(SesionGlobal.class);
        sesionGlobalMockStatic.when(SesionGlobal::getInstancia).thenReturn(sesionGlobalMock);

        archivoValido = File.createTempFile("documento_prueba", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(archivoValido)) {
            fos.write("Contenido de prueba".getBytes());
        }
    }

    @After
    public void tearDown() {
        if (sesionGlobalMockStatic != null) {
            sesionGlobalMockStatic.close();
        }
        if (archivoValido != null && archivoValido.exists()) {
            archivoValido.delete();
        }
    }

    @Test
    public void testArchivoInvalido() {
        RespuestaOperacion respuesta = service.procesarSubidaDocumento(null, "Reporte Final");
        assertTrue(respuesta.isError());
        assertEquals("No se seleccionó ningún archivo válido.", respuesta.getMensaje());
    }

    @Test
    public void testSinSesionActiva() {
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(null);

        RespuestaOperacion respuesta = service.procesarSubidaDocumento(archivoValido, "Reporte Final");
        assertTrue(respuesta.isError());
        assertEquals("Error: No hay sesión activa. Por favor inicie sesión nuevamente.", respuesta.getMensaje());
    }

    @Test
    public void testEstudianteSinExpediente() throws ExcepcionDAO {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(usuario);
        when(documentoDAOMock.obtenerIdExpedientePorUsuario(1)).thenReturn(-1);

        RespuestaOperacion respuesta = service.procesarSubidaDocumento(archivoValido, "Reporte Final");
        assertTrue(respuesta.isError());
        assertEquals("El estudiante actual no tiene un expediente asignado.", respuesta.getMensaje());
    }

    @Test
    public void testSubidaExitosa() throws ExcepcionDAO {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        when(sesionGlobalMock.getUsuarioActual()).thenReturn(usuario);
        when(documentoDAOMock.obtenerIdExpedientePorUsuario(1)).thenReturn(100);
        when(documentoDAOMock.obtenerIdCatalogoDocumento("Reporte Final")).thenReturn(5);
        when(documentoDAOMock.registrarDocumento(any(Documento.class))).thenReturn(true);

        RespuestaOperacion respuesta = service.procesarSubidaDocumento(archivoValido, "Reporte Final");
        assertFalse(respuesta.isError());
        assertEquals("Archivo cargado exitosamente", respuesta.getMensaje());

        verify(documentoDAOMock, times(1)).registrarDocumento(any(Documento.class));
    }
}
