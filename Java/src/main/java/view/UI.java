package view;

import logic.RequestHandler;
import java.io.IOException;


import fxmlController.MainController;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.application.Application;
import javafx.stage.*;
import javafx.fxml.*;


public class UI extends Application {

	public UI() {}
	
	public void go(RequestHandler rh) {
		launch();
	}

	@Override
	public void start(final Stage primaryStage) throws IOException {
		
		primaryStage.setTitle("Face Analysis");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		
		Parent root = loader.load();
		
		MainController mainController = (MainController)loader.getController();
		
		mainController.setStage(primaryStage);
		
		Scene scene = new Scene(root, 1400, 800);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}