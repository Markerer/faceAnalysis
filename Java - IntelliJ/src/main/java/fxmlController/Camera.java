package fxmlController;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class Camera {

    private boolean stopCamera = true;
    private Webcam selWebCam = null;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

    void initializeWebCam() {

        Task<Void> webCamIntilizer = new Task<Void>() {

            @Override
            protected Void call() {

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

    void startWebCamStream(ImageView imageView) {

        stopCamera = false;
        openCamera();
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() {

                while (!stopCamera && selWebCam.isOpen()) {
                    try {
                        if ((grabbedImage = selWebCam.getImage()) != null) {

                            Platform.runLater(() -> {
                                final Image mainimage = SwingFXUtils
                                        .toFXImage(grabbedImage, null);
                                imageProperty.set(mainimage);
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
        imageView.imageProperty().bind(imageProperty);

    }

    void closeCamera() {
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    private void openCamera() {
        if (selWebCam != null) {
            selWebCam.open();
        }
    }

    BufferedImage getWebcamImage(){
        return selWebCam.getImage();
    }

    void setStopCamera(boolean value){
        this.stopCamera = value;
    }

    boolean isStopCamera(){
        return stopCamera;
    }

    boolean isWebcamNull(){
        return selWebCam == null;
    }
}
