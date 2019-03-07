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

    public void initializeWebCam() {

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

    public void startWebCamStream(ImageView imageView) {

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
        imageView.imageProperty().bind(imageProperty);

    }

    public void closeCamera() {
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    public void openCamera() {
        if (selWebCam != null) {
            selWebCam.open();
        }
    }

    public BufferedImage getWebcamImage(){
        return selWebCam.getImage();
    }

    public void setStopCamera(boolean value){
        this.stopCamera = value;
    }

    public boolean isStopCamera(){
        return stopCamera;
    }

    public boolean isWebcamNull(){
        return selWebCam == null;
    }
}
