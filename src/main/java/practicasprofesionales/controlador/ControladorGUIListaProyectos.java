/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.EstudianteDAO;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.dao.SolicitudPracticaDAO;
import practicasprofesionales.modelo.pojo.Proyecto;
import practicasprofesionales.modelo.pojo.SolicitudEstudiante;
import practicasprofesionales.utilidades.SesionGlobal;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIListaProyectos implements Initializable {

    @FXML
    private TableView<Proyecto> tabla_proyectos;
    @FXML
    private TableColumn<Proyecto, String> col_nombre;
    @FXML
    private TableColumn<Proyecto, String> col_organizacion;
    @FXML
    private TableColumn<Proyecto, Integer> col_cupos;
    @FXML
    private TableColumn<Proyecto, String> col_prioridad;
    @FXML
    private Button btn_masInformacion;
    @FXML
    private Button btn_guardar;
    @FXML
    private Label lbl_solicitudNoPermitida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_organizacion.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        col_cupos.setCellValueFactory(new PropertyValueFactory<>("cupo"));
        tabla_proyectos.setEditable(true);
        col_prioridad.setCellValueFactory(new PropertyValueFactory<>("prioridadSeleccionada"));

        configurarColumnaPrioridad();

        cargarProyectos();
    }

    private void cargarProyectos() {
        try {
            int idUsuarioLogueado = SesionGlobal.getInstancia().getUsuarioActual() != null
                    ? SesionGlobal.getInstancia().getUsuarioActual().getIdUsuario() : -1;
            int idEstudianteActual = -1;

            if (idUsuarioLogueado != -1) {
                idEstudianteActual = EstudianteDAO.obtenerIdEstudiantePorUsuario(idUsuarioLogueado);
            }

            Map<Integer, Integer> selecciones = new HashMap<>();
            if (idEstudianteActual != -1) {
                selecciones = SolicitudPracticaDAO.obtenerSeleccionesEstudiante(idEstudianteActual);
            }

            ProyectoDAO dao = new ProyectoDAO();
            List<Proyecto> lista = dao.obtenerProyectosDisponibles();

            for (Proyecto p : lista) {
                if (selecciones.containsKey(p.getIdProyecto())) {
                    p.setPrioridadSeleccionada(String.valueOf(selecciones.get(p.getIdProyecto())));
                } else {
                    p.setPrioridadSeleccionada("Ninguna");
                }
            }
            boolean yaTieneSolicitud = SolicitudPracticaDAO.estudianteTieneSolicitud(idEstudianteActual);
            if (yaTieneSolicitud == true) {
                tabla_proyectos.setEditable(false);
                btn_guardar.setDisable(true);
                lbl_solicitudNoPermitida.setText("Ya hiciste tu solicitud,"
                                                  + " no puedes realizar otra");
            } else {
                tabla_proyectos.setEditable(true);
            }
            tabla_proyectos.setItems(FXCollections.observableArrayList(lista));

        } catch (ExcepcionDAO e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error",
                    "No se pudieron cargar los proyectos.",
                    Alert.AlertType.ERROR);
        }
    }

    private void abrirDetalleProyecto(Proyecto proyecto, ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Pane parentPane = (Pane) source.getScene().lookup("#pn_principal");

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/practicasprofesionales/vista/solicitarproyecto/GUIDetalleProyecto.fxml"));
            Region subVista = loader.load();

            ControladorGUIDetalleProyecto controlador = loader.getController();
            controlador.inicializarDatos(proyecto);

            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());

            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_masInformacion(ActionEvent event) {
        Proyecto proyectoSeleccionado
                = tabla_proyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {
            abrirDetalleProyecto(proyectoSeleccionado, event);
        } else {
            Utilidades.mostrarAlertaSimple("Atención",
                    "Por favor selecciona un proyecto de la tabla primero.",
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btn_guardarOnAction(ActionEvent event) {
        List<SolicitudEstudiante> listaAEnviar = new ArrayList<>();
        if (SesionGlobal.getInstancia().getUsuarioActual() == null) {
            Utilidades.mostrarAlertaSimple("Error de sesión",
                    "No se detectó un usuario activo.", Alert.AlertType.ERROR);
            return;
        }
        int idUsuarioLogueado
                = SesionGlobal.getInstancia().getUsuarioActual().getIdUsuario();
        int idEstudianteActual;

        try {
            idEstudianteActual = EstudianteDAO.obtenerIdEstudiantePorUsuario(
                    idUsuarioLogueado);
            if (idEstudianteActual == -1) {
                Utilidades.mostrarAlertaSimple("Error",
                        "El usuario no tiene un perfil de estudiante asociado.",
                        Alert.AlertType.ERROR);
                return;
            }
        } catch (ExcepcionDAO e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "No se pudo consultar la información del estudiante.",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
            return;
        }
        for (Proyecto p : tabla_proyectos.getItems()) {
            if (p.getPrioridadSeleccionada() != null
                    && !p.getPrioridadSeleccionada().equals("Ninguna")) {
                SolicitudEstudiante se = new SolicitudEstudiante();
                se.setProyecto(p);
                se.setPrioridad(Integer.parseInt(p.getPrioridadSeleccionada()));
                listaAEnviar.add(se);
            }
        }
        int totalProyectosEnTabla = tabla_proyectos.getItems().size();
        if (!sonPrioridadesValidas(listaAEnviar, totalProyectosEnTabla)) {
            return;
        }
        if(tieneSolicitud(idEstudianteActual) == true){
            return;
        }
        try {
            SolicitudPracticaDAO dao = new SolicitudPracticaDAO();
            boolean exito = dao.registrarSolicitudMultipe(idEstudianteActual,
                    listaAEnviar);
            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito",
                        "Tus solicitudes han sido registradas correctamente.",
                        Alert.AlertType.INFORMATION);
                cargarProyectos();
            }
        } catch (ExcepcionDAO e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Ocurrió un error al guardar en la base de datos.",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean sonPrioridadesValidas(List<SolicitudEstudiante> lista,
            int totalProyectos) {
        if (lista.size() != totalProyectos) {
            Utilidades.mostrarAlertaSimple("Atención",
                    "Debes asignar una prioridad a TODOS los"
                    + " proyectos de la lista antes de guardar.",
                    Alert.AlertType.WARNING);
            return false;
        }
        Set<Integer> prioridadesUnicas = new HashSet<>();
        for (SolicitudEstudiante se : lista) {
            if (!prioridadesUnicas.add(se.getPrioridad())) {
                Utilidades.mostrarAlertaSimple("Prioridad duplicada",
                        "Has asignado la prioridad " + se.getPrioridad()
                        + " a más de un proyecto. Las prioridades deben ser únicas.",
                        Alert.AlertType.WARNING);
                return false;
            }
        }

        return true;
    }

    private void configurarColumnaPrioridad() {
        col_prioridad.setCellFactory(tc -> new TableCell<Proyecto, String>() {
            private ComboBox<String> combo;
            private boolean isInitializing = false;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (combo == null) {
                        combo = new ComboBox<>();
                        combo.valueProperty().addListener((obs, oldVal, newVal) -> {
                            if (!isInitializing && isEditing() && newVal != null) {
                                commitEdit(newVal);
                            }
                        });
                        combo.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                            if (!isNowFocused && isEditing()) {
                                cancelEdit();
                            }
                        });
                    }

                    isInitializing = true;
                    List<String> disponibles = new ArrayList<>();
                    disponibles.add("Ninguna");
                    int total = getTableView().getItems().size();
                    Proyecto pActual = getTableView().getItems().get(getIndex());
                    for (int i = 1; i <= total; i++) {
                        String val = String.valueOf(i);
                        boolean enUso = false;
                        for (Proyecto p : getTableView().getItems()) {
                            if (p != pActual && val.equals(p.getPrioridadSeleccionada())) {
                                enUso = true;
                                break;
                            }
                        }
                        if (!enUso) {
                            disponibles.add(val);
                        }
                    }
                    combo.setItems(FXCollections.observableArrayList(disponibles));
                    combo.getSelectionModel().select(getItem() != null ? getItem() : "Ninguna");
                    isInitializing = false;

                    setText(null);
                    setGraphic(combo);
                    combo.requestFocus();
                    combo.show();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? getItem() : "Ninguna");
                setGraphic(null);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        setText(null);
                        setGraphic(combo);
                    } else {
                        setText(item != null ? item : "Ninguna");
                        setGraphic(null);
                    }
                }
            }
        });

        col_prioridad.setOnEditCommit(event -> {
            Proyecto p = event.getRowValue();
            p.setPrioridadSeleccionada(event.getNewValue());
        });
    }

    private boolean tieneSolicitud(int idEstudianteActual){
        try {
            if (SolicitudPracticaDAO.estudianteTieneSolicitud(idEstudianteActual)) {
                Utilidades.mostrarAlertaSimple("Solicitud Existente",
                    "Ya has enviado una solicitud de proyectos previamente.",
                    Alert.AlertType.WARNING);
                return true;
            }
        } catch (ExcepcionDAO e) {
            Utilidades.mostrarAlertaSimple("Error de Conexión",
                    "No se pudo verificar el estado actual de tus solicitudes.",
                    Alert.AlertType.ERROR);
            return true;
        }
        return false;
    }
}
