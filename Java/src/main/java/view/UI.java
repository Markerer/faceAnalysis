package view;

import javafx.geometry.*;
import javafx.stage.Screen;
import logic.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import interfaces.RequestListener;
import interfaces.UIListener;
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
public class UI extends Application implements RequestListener {

	ImageView imageView;
	static Label description;

	static RequestHandler rh2;
	DetectedFace detectedFace;
	File img;

	private List<UIListener> listeners;

	public UI() {
		listeners = new ArrayList<UIListener>();
	}
	
	public void go(RequestHandler rh) {
		rh2 = rh;
		rh.addListener(this);
		launch();
		
	}


	public void addListener(UIListener toAdd) {
		System.out.println("listener id1: " + listeners);
		System.out.println("listener added");
		listeners.add(toAdd);
		System.out.println("listener id1: " + listeners);
	}

	@Override
	public void start(final Stage primaryStage) {

		listeners.add(rh2);
		System.out.println(listeners);
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
					for (UIListener il : listeners) {
						System.out.println("kép hozzáadva");
						il.imageAdded(file);
					}
					// vbox.setVisible(true);
				}
			}
		});

		Button requestButton = new Button("Analizálás!");
		requestButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				System.out.println("gomb lenyomva");
				System.out.println("listener id2: " + listeners);
				description.setText("kurv");
				for (UIListener il : listeners) {
					System.out.println("gomb lenyomva átadva");
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
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
		((VBox) scene.getRoot()).getChildren().addAll(imageView, description);
		((VBox) scene.getRoot()).getChildren().addAll(requestButton);
		((VBox) scene.getRoot()).setAlignment(Pos.TOP_CENTER);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void setLabel(String message) {
		description.setText(message);
	}

	private void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Válasszon egy képet...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

	}

	public void requestFailed() {
		setLabel("Nem sikerült a kérés!");

	}

	public void requestSuccess(DetectedFace detectedFace) {
		this.detectedFace = detectedFace;
		String message = "";
		message += "Életkor: " + this.detectedFace.faceAttributes().age() + "\n";
		message += "Mosoly: " + this.detectedFace.faceAttributes().smile() + "\n";
		message += "Nem: " + this.detectedFace.faceAttributes().gender().toString() + "\n";
		message += "Szemüveg: " + this.detectedFace.faceAttributes().glasses().toString() + "\n";
		message += "Szakáll: " + this.detectedFace.faceAttributes().facialHair().beard() + "\n";
		message += "Bajusz: " + this.detectedFace.faceAttributes().facialHair().moustache() + "\n";
		message += "Oldalszakáll: " + this.detectedFace.faceAttributes().facialHair().sideburns() + "\n";
		message += "Kopasz: " + this.detectedFace.faceAttributes().hair().bald() + "\n";
		//message += this.detectedFace.toString();
		System.out.println(message);
		description.setText(message);
	}

}