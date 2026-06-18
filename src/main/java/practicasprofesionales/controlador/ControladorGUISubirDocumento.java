/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;
import practicasprofesionales.modelo.servicios.SubirDocumentoService;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUISubirDocumento implements Initializable {

    @FXML
    private TextArea txt_contenido;
    @FXML
    private StackPane pn_dropzone;
    @FXML
    private Button btn_seleccionarDocumento;
    @FXML
    private Button btn_subir;
    @FXML
    private Label lbl_tipoDocumentoTitulo;
    @FXML
    private Label lbl_archivosSubidos;

    private String tipoDocumento;
    private File archivoSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_subir.setDisable(true);
    }

    public void inicializarDatos(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        if (lbl_tipoDocumentoTitulo != null) {
            lbl_tipoDocumentoTitulo.setText("Documento seleccionado: " + tipoDocumento);
        }
    }

    @FXML
    private void btn_seleccionarDocumento(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar documento");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Documentos (PDF, DOCX, TXT)", "*.pdf", "*.docx", "*.txt"),
                    new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
            );

            Node fuente = (Node) event.getSource();
            Stage stage = (Stage) fuente.getScene().getWindow();

            File archivo = fileChooser.showOpenDialog(stage);

            if (archivo != null) {
                this.archivoSeleccionado = archivo;
                lbl_archivosSubidos.setText("Archivos subidos: 1 (" + archivo.getName() + ")");
                btn_subir.setDisable(false);
            }
        } catch (Exception e) {
            boolean confirmacion = Utilidades.configurarAlerta(
                    "Error al abrir explorador",
                    "No se pudo abrir el explorador de archivos",
                    "Ocurrió un error inesperado al intentar acceder a los archivos de su computador.",
                    "Intentar de nuevo", "Cancelar", Alert.AlertType.ERROR);

            if (confirmacion) {
                btn_seleccionarDocumento(event);
            }
        }
    }

    @FXML
    private void btn_cancelar(ActionEvent event) {

        boolean confirmacion = Utilidades.configurarAlerta("Confirmación de Cancelación",
                "¿Seguro desea cancelar el proceso?",
                "Los cambios y documentos seleccionados no se guardarán.",
                "Sí",
                "No",
                Alert.AlertType.CONFIRMATION);

        if (confirmacion) {
            regresarAListaDocumentos(event);
        }
    }

    @FXML
    private void btn_subir(ActionEvent event) {
        SubirDocumentoService service = new SubirDocumentoService();
        RespuestaOperacion respuesta = service.procesarSubidaDocumento(archivoSeleccionado, tipoDocumento);

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            regresarAListaDocumentos(event);
        } else {
            Utilidades.mostrarAlertaSimple("Error al subir", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void regresarAListaDocumentos(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Pane parentPane = (Pane) source.getScene().lookup("#pn_principal");

            Region subVista = (Region) FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUIListaSubirDocumentos.fxml"));

            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());

            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
