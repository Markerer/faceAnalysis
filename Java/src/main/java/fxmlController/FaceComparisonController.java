package fxmlController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class FaceComparisonController implements Initializable {

	// Felesleges lesz a tárolása, elég lesz a betöltéskor átadni...
	private File imageFile;
	
	@FXML
	private MenuController menuController;
	
	@FXML
	private ImageView leftImage; 
	
	@FXML
	private ImageView rightImage;
	
	@FXML
	private Label comparisonDescription;
	
	@FXML
	private Button webcamButton;
	
	@FXML
	private Button leftImageButton;
	
	@FXML
	private Button rightImageButton;
	
	@FXML
	private Button comparisonButton;
	
	
	@FXML
	private void onLeftImageButtonPressed(final ActionEvent event) {
		this.fileChoosing(this.leftImage);
	}
	
	@FXML
	private void onRightImageButtonPressed(final ActionEvent event) {
		this.fileChoosing(this.rightImage);
	}
	
	@FXML
	private void onWebcamButtonPressed(final ActionEvent event) {
		// TODO webkamera megnyitása, ha van ofc...
		this.webcamButton.setText("Kép készítése");
		// TODO kép átadása a Controller osztályon keresztül
		
	}
	
	
	// TODO Controlleren keresztül REST Api hívás...
	@FXML
	private void onComparisonButtonPressed(final ActionEvent event) {
		
	}
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
				
	}

	private void fileChoosing(final ImageView imageView) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Válasszon egy képet...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

		
		File file = fileChooser.showOpenDialog(MenuController.getStage());
		if (file != null) {
			this.imageFile = file;
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
	}
	
	
}
