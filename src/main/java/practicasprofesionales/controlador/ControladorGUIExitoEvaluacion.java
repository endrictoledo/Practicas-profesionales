package practicasprofesionales.controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionales.utilidades.Utilidades;

public class ControladorGUIExitoEvaluacion implements Initializable {

    private Button btn_descargar;
    private byte[] archivoBytes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializarDatos(byte[] bytes) {
        this.archivoBytes = bytes;
    }

    @FXML
    private void btn_descargar(ActionEvent event) {
        if (archivoBytes == null) {
            Utilidades.mostrarAlertaSimple("Error", "No hay datos de archivo para descargar.", Alert.AlertType.ERROR);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar evaluación OV");
        fileChooser.setInitialFileName("Evaluacion_OV.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt"));

        Stage stage = (Stage) btn_descargar.getScene().getWindow();
        File archivoDestino = fileChooser.showSaveDialog(stage);

        if (archivoDestino != null) {
            try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                fos.write(archivoBytes);
                Utilidades.mostrarAlertaSimple("Éxito", "El archivo se ha descargado correctamente en tu PC.", Alert.AlertType.INFORMATION);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/GUIMenuPracticante.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                Utilidades.mostrarAlertaSimple("Error de Descarga", "No se pudo descargar correctamente el archivo. Intenta de nuevo.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }

    }
}
