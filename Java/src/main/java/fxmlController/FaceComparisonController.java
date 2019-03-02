package fxmlController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FaceComparisonController implements Initializable {

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
		
	}
	
	@FXML
	private void onRightImageButtonPressed(final ActionEvent event) {
		
	}
	
	@FXML
	private void onWebcamButtonPressed(final ActionEvent event) {
		
	}
	
	@FXML
	private void onComparisonButtonPressed(final ActionEvent event) {
		
	}
	
	
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
				
	}

	
	
	
}
