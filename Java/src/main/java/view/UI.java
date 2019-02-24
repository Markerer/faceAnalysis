package view;

import javafx.geometry.*;
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import interfaces.ImageListener;
import javafx.scene.image.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.*;



@SuppressWarnings("restriction")
public class UI extends Application {

	ImageView imageView;
	Label description;
	
	DetectedFace detectedFace;
	
	File img;
	
	private List<ImageListener> listeners = new ArrayList<ImageListener>();
	
	 public void addListener(ImageListener toAdd) {
	        listeners.add(toAdd);
	    }
    
    @Override
    public void start(final Stage primaryStage) {
    	
    	imageView = new ImageView();
    	description = new Label();
        primaryStage.setTitle("Face Analysis");
        
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);

        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fájl");
        Menu menuActions = new Menu("Lehetőségek");
        
        MenuItem add = new MenuItem("Kép megnyitása");
            add.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                	File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	img = file;
                    	Image image = new Image(file.toURI().toString());
                    	imageView.setImage(image);
                    	for (ImageListener il : listeners) {
                            il.imageAdded(file);
                    	}
                    	//vbox.setVisible(true);
                    }                   
                }
            });        
            
        Button requestButton = new Button("Analizálás!");
        requestButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				for (ImageListener il : listeners) {
                    il.requestButtonPressed();
            	}				
			}
        	
        });
        
        MenuItem exit = new MenuItem("Kilépés");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        
        menuFile.getItems().addAll(add, new SeparatorMenuItem(), exit);
        
        menuBar.getMenus().addAll(menuFile, menuActions);
        
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.JUSTIFY);
        
       
        Scene scene = new Scene(new VBox(), 800, 600);
        ((VBox)scene.getRoot()).getChildren().addAll(menuBar);
        ((VBox)scene.getRoot()).getChildren().addAll(imageView, description);
        ((VBox)scene.getRoot()).getChildren().addAll(requestButton);
        ((VBox)scene.getRoot()).setAlignment(Pos.TOP_CENTER);
      
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void go(String[] args) {
    	launch(args);
    }
    
    public void setDetectedFace(DetectedFace face) {
    	detectedFace = face;
    	setLabel();
    }
    
    
    private void setLabel() {
    	
    }
    
    
    private void configureFileChooser(final FileChooser fileChooser) {
    	fileChooser.setTitle("Válasszon egy képet...");
    	fileChooser.setInitialDirectory(
    			new File(System.getProperty("user.home"))
    			);
    	 fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("All Images", "*.*"),
                 new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                 new FileChooser.ExtensionFilter("PNG", "*.png")
             );
    	
    }
    
}