package fxmlController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.UI;
import wrappers.CustomDetectedFace;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import javafx.event.ActionEvent;
import javafx.fxml.*;


public class MainController implements Initializable{

	
	private File imageFile;
	
	@FXML
	private MenuController menuController;
	
	@FXML
	private Text description;
	
	@FXML
	private ImageView image;
	
	@FXML
	private Button analysisButton;
	
	@FXML 
	private Button imageChooserButton;
	
	@FXML
	private void onImageChooserButtonPressed(final ActionEvent event) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Válasszon egy képet...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

		
		File file = fileChooser.showOpenDialog(MenuController.getStage());
		if (file != null) {
			this.imageFile = file;
			Image image = new Image(file.toURI().toString());
			this.image.setImage(image);
			
		}
	}
	
	@FXML
	private void onAnalyzeButtonPressed(final ActionEvent event) {
		// interfészes cuccos, ehelyett Controlleren keresztül hívás
		// Listát kapunk vissza, jelenleg csak az 1. elemmel foglalkozunk
		// TODO bővítési lehetőség (több arc tulajdonságának kiírása)
		List<DetectedFace> detectedFaces = new ArrayList<DetectedFace>();
			detectedFaces = UI.controller.AnalyseLocalPicture(imageFile);
		if(detectedFaces.size() > 0) {
			
			DetectedFace df = detectedFaces.get(0);
			System.out.println(detectedFaces.size());
			System.out.println(df.toString());
			CustomDetectedFace customDetectedFace = new CustomDetectedFace(df);
			setDescription(customDetectedFace.toString());
		} else {
			setDescription("Nem található felismerhető arc a képen.");
		}
	}
	

	public void setDescription(String message) {
		System.out.println(message);
		description.setText(message);
	}
	
	
	public void setStage(final Stage stage) {
		this.menuController.setStage(stage);
	}

	// hivatalos inicializáló whatever, lehet nem is kellene...
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
}
