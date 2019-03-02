package fxmlController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.UI;
import wrappers.CustomDetectedFace;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import javafx.event.ActionEvent;
import javafx.fxml.*;


public class MainController implements Initializable{

	
	private File imageFile;
	
	@FXML
	private MenuController menuController;
	
	@FXML
	private Label description;
	
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
		DetectedFace df = UI.controller.AnalyseLocalPicture(imageFile);
		if (df!=null)
			setDescription(df);
	}
	
	
	// Controller osztály meghívhatja majd a kérés visszatérésekor...
	// Az eredmény feldolgozása lehet akár a Controllerben is (DetectedFace)
	// azaz, akkor csak String-et kell átadni paraméterben 
	// így nem kellene tárolni a detectedFace-t itt is...
	public void setDescription(DetectedFace detectedFace) {
		CustomDetectedFace customDetectedFace = new CustomDetectedFace(detectedFace);
		String message = customDetectedFace.toString();
		System.out.println(message);
		description.setText(message);
	}
	
	
	public void setStage(final Stage stage) {
		this.menuController.setStage(stage);
	}

	
	public void init() {
		// Controller osztály beállítás? 
	}

	// hivatalos inicializáló whatever, lehet nem is kellene...
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
}
