package view;

import logic.Controller;
import logic.RequestHandler;
import java.io.IOException;


import fxmlController.MainController;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.application.Application;
import javafx.stage.*;
import javafx.fxml.*;


public class UI extends Application {

	public static Controller controller;
	
	public UI() {}
	
	public void go() {
		launch();
	}

	@Override
	public void start(final Stage primaryStage) throws IOException {
		
		controller = new Controller(new RequestHandler());
		
		primaryStage.setTitle("Face Analysis");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		
		Parent root = loader.load();
		
		MainController mainController = (MainController)loader.getController();
		
		mainController.setStage(primaryStage);
		
		Scene scene = new Scene(root, 1600, 900);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}