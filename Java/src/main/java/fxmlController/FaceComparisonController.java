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
	
	private boolean stopCamera = true;
	private Webcam selWebCam = null;
	private BufferedImage grabbedImage;
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
	
	
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
	
	private class WebCamInfo {

		private String webCamName;
		private int webCamIndex;

		public String getWebCamName() {
			return webCamName;
		}

		public void setWebCamName(String webCamName) {
			this.webCamName = webCamName;
		}

		public int getWebCamIndex() {
			return webCamIndex;
		}

		public void setWebCamIndex(int webCamIndex) {
			this.webCamIndex = webCamIndex;
		}

		@Override
		public String toString() {
			return webCamName;
		}
	}
	
	protected void initializeWebCam() {

		Task<Void> webCamIntilizer = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				if (selWebCam == null) {
					selWebCam = Webcam.getDefault();
					selWebCam.setViewSize(WebcamResolution.VGA.getSize());
				} else {
					closeCamera();
					selWebCam = Webcam.getDefault();
					selWebCam.setViewSize(WebcamResolution.VGA.getSize());
				}
				
				return null;
			}

		};

		new Thread(webCamIntilizer).start();
	}
	
	protected void startWebCamStream() {

		stopCamera = false;
		openCamera();
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				while (!stopCamera && selWebCam.isOpen()) {
					try {
						if ((grabbedImage = selWebCam.getImage()) != null) {

							Platform.runLater(new Runnable() {

								public void run() {
									final Image mainimage = SwingFXUtils
										.toFXImage(grabbedImage, null);
									imageProperty.set(mainimage);
								}
							});

							grabbedImage.flush();

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return null;
			}

		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		rightImage.imageProperty().bind(imageProperty);

	}
	
	private void closeCamera() {
		if (selWebCam != null) {
			selWebCam.close();
		}
	}
	
	private void openCamera() {
		if(selWebCam != null) {
			selWebCam.open();
		}
	}
	
	
	@FXML
	private void onWebcamButtonPressed(final ActionEvent event) throws IOException {
		
	
		if(stopCamera && selWebCam != null) {
			this.webcamButton.setText("Kép készítése");
			startWebCamStream();
			rightImageButton.setDisable(true);
		} else {
			if(!stopCamera && selWebCam != null) {
				BufferedImage image = selWebCam.getImage();
				ImageIO.write(image, "PNG", new File("captured.png"));
				this.webcamButton.setText("Webkamera megnyitása");
				closeCamera();
				rightImageButton.setDisable(false);
				
				rightImageFile = new File("captured.png");
				Image img = new Image(rightImageFile.toURI().toString());
				rightImage.imageProperty().unbind();
				rightImage.setImage(img);
				stopCamera = true;
			}
		}		
	}
	
	
	// TODO Controlleren keresztül REST Api hívás...
	@FXML
	private void onComparisonButtonPressed(final ActionEvent event) {
		if(leftImageFile != null && rightImageFile != null) {
			comparisonDescription.setText(UI.controller.CompareTwoPictures(leftImageFile, rightImageFile));
		}
	}
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
		int webCamCounter = 0;
		for (Webcam webcam : Webcam.getWebcams()) {
			WebCamInfo webCamInfo = new WebCamInfo();
			webCamInfo.setWebCamIndex(webCamCounter);
			webCamInfo.setWebCamName(webcam.getName());
			options.add(webCamInfo);
			webCamCounter++;
		}
		// A legelső webcamot választjuk ki egyelőre, bővítési lehetőség (választás) egy ComboBox-al mondjuk...
		if(options.size() > 0) {
			initializeWebCam();
		}
	}

	private File fileChoosing(final ImageView imageView) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Válasszon egy képet...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

		
		File file = fileChooser.showOpenDialog(MenuController.getStage());
		if (file != null) {
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
			return file;
		}
		return null;
	}	
}
