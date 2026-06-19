/*
 * Pruebas unitarias para SolicitarProyectoService utilizando Mockito.
 */
package practicasprofesionales.modelo.servicios;

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
import practicasprofesionales.modelo.dao.EstudianteDAO;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.dao.SolicitudPracticaDAO;
import practicasprofesionales.modelo.pojo.Proyecto;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.SesionGlobal;

public class SolicitarProyectoServiceTest {

    @Mock
    private ProyectoDAO proyectoDAOMock;

    @Mock
    private SolicitudPracticaDAO solicitudDAOMock;

    @InjectMocks
    private SolicitarProyectoService service;

    private MockedStatic<SesionGlobal> sesionGlobalMockStatic;
    private MockedStatic<EstudianteDAO> estudianteDAOMockStatic;
    private SesionGlobal sesionGlobalMock;
    private Proyecto proyectoPrueba;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sesionGlobalMock = mock(SesionGlobal.class);
        sesionGlobalMockStatic = mockStatic(SesionGlobal.class);
        sesionGlobalMockStatic.when(SesionGlobal::getInstancia).thenReturn(sesionGlobalMock);

        estudianteDAOMockStatic = mockStatic(EstudianteDAO.class);

        proyectoPrueba = new Proyecto();
        proyectoPrueba.setIdProyecto(10);
        proyectoPrueba.setNombre("Desarrollo de App");
    }

    @After
    public void tearDown() {
        if (sesionGlobalMockStatic != null) {
            sesionGlobalMockStatic.close();
        }
        if (estudianteDAOMockStatic != null) {
            estudianteDAOMockStatic.close();
        }
    }

    @Test
    public void testSinSesionActiva() {
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(null);

        RespuestaOperacion respuesta = service.solicitarProyecto(proyectoPrueba);
        assertTrue(respuesta.isError());
        assertEquals("Error de sesión: Inicie sesión de nuevo.", respuesta.getMensaje());
    }

    @Test
    public void testSinIdEstudiante() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(usuario);
        
        estudianteDAOMockStatic.when(() -> EstudianteDAO.obtenerIdEstudiantePorUsuario(1)).thenReturn(-1);

        RespuestaOperacion respuesta = service.solicitarProyecto(proyectoPrueba);
        assertTrue(respuesta.isError());
        assertEquals("Error de sistema: Estudiante no encontrado.", respuesta.getMensaje());
    }

    @Test
    public void testSinCuposDisponibles() throws ExcepcionDAO {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(usuario);
        
        estudianteDAOMockStatic.when(() -> EstudianteDAO.obtenerIdEstudiantePorUsuario(1)).thenReturn(50);
        when(proyectoDAOMock.verificarCupos(10, "Desarrollo de App")).thenReturn(0);

        RespuestaOperacion respuesta = service.solicitarProyecto(proyectoPrueba);
        assertTrue(respuesta.isError());
        assertEquals("Lo sentimos, el cupo de este proyecto llegó al límite", respuesta.getMensaje());
    }

    @Test
    public void testSolicitudExitosa() throws ExcepcionDAO {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(sesionGlobalMock.getUsuarioActual()).thenReturn(usuario);
        
        estudianteDAOMockStatic.when(() -> EstudianteDAO.obtenerIdEstudiantePorUsuario(1)).thenReturn(50);
        when(proyectoDAOMock.verificarCupos(10, "Desarrollo de App")).thenReturn(2);
        when(solicitudDAOMock.registrarSolicitud(50, proyectoPrueba)).thenReturn(true);

        RespuestaOperacion respuesta = service.solicitarProyecto(proyectoPrueba);
        assertFalse(respuesta.isError());
        assertEquals("El proyecto fue agregado con éxito", respuesta.getMensaje());
        
        verify(solicitudDAOMock, times(1)).registrarSolicitud(50, proyectoPrueba);
    }
}
