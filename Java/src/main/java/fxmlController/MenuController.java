package fxmlController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent;
import javafx.fxml.*;

public class MenuController implements Initializable {

	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private void onExitItemPressed(final ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	private void onBackToMainItemPressed(final ActionEvent event) {
		
	}
	
	@FXML
	private void onFaceComparisonItemPressed(final ActionEvent event) {
		
	}	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		menuBar.setFocusTraversable(true);		
	}

}
