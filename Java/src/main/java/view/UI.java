package view;

import javafx.geometry.*;
import javafx.stage.Screen;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private Desktop desktop = Desktop.getDesktop();
    
    @Override
    public void start(final Stage primaryStage) {
    	
    	final ImageView imageView = new ImageView();
    	final Label description = new Label();
        primaryStage.setTitle("Face Analysis");
        
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);

        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fájl");
        Menu menuActions = new Menu("Lehetõségek");
        
        MenuItem add = new MenuItem("Kép megnyitása");
            add.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                	File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	Image image = new Image(file.toURI().toString());
                    	imageView.setImage(image);
                    	//vbox.setVisible(true);
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
        ((VBox)scene.getRoot()).setAlignment(Pos.TOP_CENTER);
      
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void go(String[] args) {
    	launch(args);
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
    
    
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                UI.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}