package fxmlController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.*;

public class MenuController implements Initializable {

	
	private static Stage actualStage;
	
	@FXML
	private MenuBar menuBar;
	
	/* A start() függvény lefutásakor inicializáljuk a main.fxml-t,
	   majd a MainController-en keresztül átadjuk a Stage objektumot 
	   a MenuControllernek, így tudjuk váltogatni a Scene-ket
	   új ablak nyitása nélkül. */
	void setStage(final Stage stage) {
		MenuController.actualStage = stage;
	}
	
	// Getter, hogy a többi Controller is el tudja érni...
	static Stage getStage() {
		return MenuController.actualStage;
	}
	
	
	@FXML
	private void onExitItemPressed(final ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	private void onBackToMainItemPressed(final ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
		Scene scene = new Scene(root, 1600, 900);
		actualStage.setScene(scene);
	}
	
	@FXML
	private void onFaceComparisonItemPressed(final ActionEvent event) throws IOException {
			
		Parent root = FXMLLoader.load(getClass().getResource("/faceComparison.fxml"));
		Scene scene = new Scene(root, 1600, 900);
		actualStage.setScene(scene);
	}	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
