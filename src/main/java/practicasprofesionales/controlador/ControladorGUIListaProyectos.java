/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.pojo.Proyecto;
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
    private TableColumn<Proyecto, Void> col_acciones;
    @FXML
    private Button btn_masInformacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_organizacion.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacion"));
        col_cupos.setCellValueFactory(new PropertyValueFactory<>("cupos"));

        cargarProyectos();
    }

    private void abrirDetalleProyecto(Proyecto proyecto, ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Pane parentPane = (Pane) source.getScene().lookup("#pn_principal");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/solicitarproyecto/GUIDetalleProyecto.fxml"));
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

    private void cargarProyectos() {
        try {
            ProyectoDAO dao = new ProyectoDAO();
            List<Proyecto> lista = dao.obtenerProyectosDisponibles();
            tabla_proyectos.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los proyectos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_masInformacion(ActionEvent event) {
        Proyecto proyectoSeleccionado = tabla_proyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {
            abrirDetalleProyecto(proyectoSeleccionado, event);
        } else {
            Utilidades.mostrarAlertaSimple("Atención", "Por favor selecciona un proyecto de la tabla primero.", Alert.AlertType.WARNING);
        }
    }  
    
}
