package fxmlController;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import view.UI;

public class FaceComparisonController implements Initializable {

	
	private File leftImageFile;
	private File rightImageFile;

	private Camera camera;
	
	@FXML
	private MenuController menuController;
	
	@FXML
	private ImageView leftImage; 
	
	@FXML
	private ImageView rightImage;
	
	@FXML
	private Text comparisonDescription;
	
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
		this.leftImageFile = fileChoosing(this.leftImage);
	}
	
	@FXML
	private void onRightImageButtonPressed(final ActionEvent event) {
		this.rightImageFile = fileChoosing(this.rightImage);
	}

	@FXML
	private void onWebcamButtonPressed(final ActionEvent event) throws IOException {
		
	
		if(camera.isStopCamera() && !camera.isWebcamNull()) {
			this.webcamButton.setText("Kép készítése");
			camera.startWebCamStream(rightImage);
			rightImageButton.setDisable(true);
		} else {
			if(!camera.isStopCamera() && !camera.isWebcamNull()) {
				MainController.webcamHandling(camera, this.webcamButton, rightImageButton);

				rightImageFile = new File("captured.png");
				Image img = new Image(rightImageFile.toURI().toString());
				rightImage.imageProperty().unbind();
				rightImage.setImage(img);
				camera.setStopCamera(true);
			}
		}		
	}
	

	@FXML
	private void onComparisonButtonPressed(final ActionEvent event) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if(leftImageFile != null && rightImageFile != null) {
					comparisonDescription.setText(UI.controller.CompareTwoPictures(leftImageFile, rightImageFile));
				}
				return null;
			}
		};

		Thread t = new Thread(task);
		t.start();
	}
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		camera = new Camera();
		camera.initializeWebCam();
	}

	private File fileChoosing(final ImageView imageView) {
		File file = MainController.fileChooserDialog();
		if (file != null) {
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
			return file;
		}
		return null;
	}	
}
