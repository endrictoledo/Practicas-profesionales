/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIListaProyectos implements Initializable {

    @FXML
    private TableView<practicasprofesionales.modelo.pojo.Proyecto> tabla_proyectos;
    @FXML
    private TableColumn<practicasprofesionales.modelo.pojo.Proyecto, String> col_nombre;
    @FXML
    private TableColumn<practicasprofesionales.modelo.pojo.Proyecto, String> col_organizacion;
    @FXML
    private TableColumn<practicasprofesionales.modelo.pojo.Proyecto, Integer> col_cupos;
    private TableColumn<practicasprofesionales.modelo.pojo.Proyecto, Void> col_acciones;
    @FXML
    private Button btn_masInformacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col_nombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        col_organizacion.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombreOrganizacion"));
        col_cupos.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cupos"));
        
        agregarBotonesTabla();
        cargarProyectos();
    }    

    private void agregarBotonesTabla() {
        javafx.util.Callback<TableColumn<practicasprofesionales.modelo.pojo.Proyecto, Void>, javafx.scene.control.TableCell<practicasprofesionales.modelo.pojo.Proyecto, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public javafx.scene.control.TableCell<practicasprofesionales.modelo.pojo.Proyecto, Void> call(final TableColumn<practicasprofesionales.modelo.pojo.Proyecto, Void> param) {
                return new javafx.scene.control.TableCell<>() {
                    private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Más información");

                    {
                        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2563eb; -fx-cursor: hand; -fx-underline: true;");
                        btn.setOnAction((javafx.event.ActionEvent event) -> {
                            practicasprofesionales.modelo.pojo.Proyecto proyecto = getTableView().getItems().get(getIndex());
                            abrirDetalleProyecto(proyecto, event);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        col_acciones.setCellFactory(cellFactory);
    }

    private void abrirDetalleProyecto(practicasprofesionales.modelo.pojo.Proyecto proyecto, javafx.event.ActionEvent event) {
        try {
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) source.getScene().lookup("#pn_principal");
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/practicasprofesionales/vista/solicitarproyecto/GUIDetalleProyecto.fxml"));
            javafx.scene.layout.Region subVista = loader.load();
            
            ControladorGUIDetalleProyecto controlador = loader.getController();
            controlador.initData(proyecto);
            
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
            practicasprofesionales.modelo.dao.ProyectoDAO dao = new practicasprofesionales.modelo.dao.ProyectoDAO();
            java.util.List<practicasprofesionales.modelo.pojo.Proyecto> lista = dao.obtenerProyectosDisponibles();
            tabla_proyectos.setItems(javafx.collections.FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los proyectos.", javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_masInformacion(ActionEvent event) {
    }
}
