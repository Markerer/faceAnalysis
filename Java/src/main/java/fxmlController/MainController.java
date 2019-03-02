package fxmlController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;


public class MainController implements Initializable{

	
	@FXML
	private Label description;
	
	@FXML
	private ImageView image;
	
	@FXML
	private Button analysisButton;
	
	@FXML
	private void onAnalyzeButtonPressed(final ActionEvent event) {
		
	}

	public void initialize(URL location, ResourceBundle resources) {
				
	}
	
}
