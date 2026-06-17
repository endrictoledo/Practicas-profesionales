/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import practicasprofesionales.modelo.pojo.RespuestaOperacion; 
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
    private javafx.scene.control.Label lbl_tipoDocumentoTitulo;
    @FXML
    private javafx.scene.control.Label lbl_archivosSubidos;

    private String tipoDocumento;
    private java.io.File archivoSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_subir.setDisable(true);
    }    

    public void initData(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        if (lbl_tipoDocumentoTitulo != null) {
            lbl_tipoDocumentoTitulo.setText("Documento seleccionado: " + tipoDocumento);
        }
    }

    @FXML
    private void btn_seleccionarDocumentoOnAction(javafx.event.ActionEvent event) {
        try {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleccionar documento");
            fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Documentos (PDF, DOCX, TXT)", "*.pdf", "*.docx", "*.txt"),
                new javafx.stage.FileChooser.ExtensionFilter("Todos los archivos", "*.*")
            );
            
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
            
            java.io.File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                this.archivoSeleccionado = file;
                lbl_archivosSubidos.setText("Archivos subidos: 1 (" + file.getName() + ")");
                btn_subir.setDisable(false);
            }
        } catch (Exception e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error al abrir explorador");
            alert.setHeaderText("No se pudo abrir el explorador de archivos");
            alert.setContentText("Ocurrió un error inesperado al intentar acceder a los archivos de su computador.");
            
            javafx.scene.control.ButtonType btnIntentar = new javafx.scene.control.ButtonType("Intentar de nuevo");
            javafx.scene.control.ButtonType btnCancelar = new javafx.scene.control.ButtonType("Cancelar", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
            
            alert.getButtonTypes().setAll(btnIntentar, btnCancelar);
            
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnIntentar) {
                btn_seleccionarDocumentoOnAction(event);
            }
        }
    }

    @FXML
    private void btn_cancelarOnAction(javafx.event.ActionEvent event) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Cancelación");
        alert.setHeaderText("¿Seguro desea cancelar el proceso?");
        alert.setContentText("Los cambios y documentos seleccionados no se guardarán.");
        
        javafx.scene.control.ButtonType btnSi = new javafx.scene.control.ButtonType("Sí");
        javafx.scene.control.ButtonType btnNo = new javafx.scene.control.ButtonType("No", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnSi, btnNo);
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnSi) {
            regresarAListaDocumentos(event);
        }
    }

    @FXML
    private void btn_subirOnAction(javafx.event.ActionEvent event) {
        practicasprofesionales.modelo.servicios.SubirDocumentoService service = new practicasprofesionales.modelo.servicios.SubirDocumentoService();
        RespuestaOperacion respuesta = service.procesarSubidaDocumento(archivoSeleccionado, tipoDocumento);
        
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.INFORMATION);
            regresarAListaDocumentos(event);
        } else {
            Utilidades.mostrarAlertaSimple("Error al subir", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }
    
    private void regresarAListaDocumentos(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) source.getScene().lookup("#pn_principal");
            
            javafx.scene.layout.Region subVista = (javafx.scene.layout.Region) javafx.fxml.FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUIListaSubirDocumentos.fxml"));
            
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());
            
            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
