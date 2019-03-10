package fxmlController;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FaceRectangle;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.UI;
import wrappers.CustomDetectedFace;

import javafx.scene.shape.Polygon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import javafx.event.ActionEvent;
import javafx.fxml.*;

import javax.imageio.ImageIO;


public class MainController implements Initializable {


    private List<String> imageFormats;

    private int actualDisplayedRecord = 0;

    private Camera camera;

    private List<CustomDetectedFace> customDetectedFaces;

    private File imageFile;

    @FXML
    private Button urlAnalyzeButton;

    @FXML
    private Button webcamAnalyzeButton;

    @FXML
    private Button analyzeButton;

    @FXML
    private Button webcamButton;

    @FXML
    private TextField urltextfield;

    @FXML
    private MenuController menuController;

    @FXML
    private Text description;

    @FXML
    private ImageView imageView;

    @FXML
    private Button imageChooserButton;

    @FXML
    private Polygon leftArrow;

    @FXML
    private Polygon rightArrow;


    @FXML
    private void onImageChooserButtonPressed(final ActionEvent event) {
        File file = fileChooserDialog();
        if (file != null) {
            this.imageFile = file;
            Image image = new Image(file.toURI().toString());
            this.imageView.setImage(image);
        }
    }

    static File fileChooserDialog() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Válasszon egy képet...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

        return fileChooser.showOpenDialog(MenuController.getStage());
    }

    @FXML
    private void onAnalyzeButtonPressed(final ActionEvent event) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                List<DetectedFace> detectedFaces = UI.controller.AnalyseLocalPicture(imageFile);

                return setupUI(detectedFaces);
            }
        };

        Thread t = new Thread(task);
        t.start();

    }

    public void setDescription(String message){
        description.setText(message);
        setArrowsVisibility();
    }


    public void setDescriptionFromList(int numOfRecord) {
        if(this.customDetectedFaces.size() > numOfRecord){
            description.setText(customDetectedFaces.get(numOfRecord).toString());
            setArrowsVisibility();
        }
    }

    private void setArrowsVisibility(){
        if(this.customDetectedFaces.size() > 1){
            leftArrow.setVisible(true);
            rightArrow.setVisible(true);
        } else {
            leftArrow.setVisible(false);
            rightArrow.setVisible(false);
        }
    }


    public void setStage(final Stage stage) {
        this.menuController.setStage(stage);
    }


    public void initialize(URL location, ResourceBundle resources) {
        camera = new Camera();
        camera.initializeWebCam();
        customDetectedFaces = new ArrayList<>();
        imageFormats = new ArrayList<>();
        imageFormats.add("JPG");
        imageFormats.add("PNG");
        imageFormats.add("JPEG");
        imageFormats.add("BMP");
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
    }

    private void drawRectangleOnImage(int numOfRecord) {
        if(this.customDetectedFaces.size() > numOfRecord){
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    BufferedImage newImage = ImageIO.read(imageFile);

                    FaceRectangle rectangleToDraw = customDetectedFaces.get(numOfRecord).getFaceRectangle();

                    Graphics2D graphics2D = newImage.createGraphics();
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.setStroke(new BasicStroke(10));
                    graphics2D.drawRect(
                            rectangleToDraw.left(),
                            rectangleToDraw.top(),
                            rectangleToDraw.width(),
                            rectangleToDraw.height());

                    graphics2D.dispose();


                    Image temp = SwingFXUtils.toFXImage(newImage, null);
                    imageView.setImage(temp);
                    return null;
                }
            };

            Thread t = new Thread(task);
            t.start();
        }
    }

    @FXML
    private void onWebcamButtonPressed(ActionEvent actionEvent) throws IOException {

        if(camera.isStopCamera() && !camera.isWebcamNull()) {
            this.webcamButton.setText("Kép készítése");
            camera.startWebCamStream(imageView);
            webcamAnalyzeButton.setDisable(true);
            urlAnalyzeButton.setDisable(true);
            analyzeButton.setDisable(true);
            urltextfield.setDisable(true);
        } else {
            if(!camera.isStopCamera() && !camera.isWebcamNull()) {
                webcamHandling(camera, this.webcamButton, webcamAnalyzeButton);
                urlAnalyzeButton.setDisable(false);
                analyzeButton.setDisable(false);
                urltextfield.setDisable(false);

                imageFile = new File("captured.png");
                Image img = new Image(imageFile.toURI().toString());
                imageView.imageProperty().unbind();
                imageView.setImage(img);
                camera.setStopCamera(true);
            }
        }
    }

    static void webcamHandling(Camera camera, Button webcamButton, Button webcamAnalyzeButton) throws IOException {
        BufferedImage image = camera.getWebcamImage();
        File file = new File("captured.png");
        ImageIO.write(image, "PNG", file);
        webcamButton.setText("Webkamera megnyitása");
        camera.closeCamera();
        webcamAnalyzeButton.setDisable(false);
        file.deleteOnExit();
    }

    @FXML
    public void onUrlAnalyzeButtonPressed(ActionEvent actionEvent) {
        String url = urltextfield.getText();
        if(!url.isEmpty()) {
            // megnézzük, hogy megfelelő-e a link (egy képformátumra végződik)
            String format = url.substring(Math.max(url.length() - 3, 0));
            if (!format.isEmpty()) {
                boolean correctUrl = false;
                format = format.toUpperCase();
                for (String f : imageFormats) {
                    if(f.equals(format)){
                        correctUrl = true;
                    }
                }
                if(correctUrl){
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            URL imageURL = new URL(url);
                            BufferedImage bufferedImage = ImageIO.read(imageURL);
                            File file = new File("temporaryIMG.png");
                            ImageIO.write(bufferedImage, "png", file);
                            imageFile = file;
                            Image temp = SwingFXUtils.toFXImage(bufferedImage, null);
                            imageView.setImage(temp);
                            file.deleteOnExit();
                            return null;
                        }
                    };

                    Thread t = new Thread(task);
                    t.start();

                    Task<Void> httpTask = new Task<Void>() {
                        @Override
                        protected Void call() {
                            List<DetectedFace> detectedFaces = UI.controller.AnalysePictureURL(url);

                            return setupUI(detectedFaces);
                        }
                    };

                    Thread th = new Thread(httpTask);
                    th.start();
                } else {
                    setDescription("Érvénytelen hivatkozás.");
                }
            }
        }
    }

    private Void setupUI(List<DetectedFace> detectedFaces) {
        if (detectedFaces.size() > 0) {
            // Removing the old elements if any
            customDetectedFaces.clear();

            // adding the new ones
            for (DetectedFace df : detectedFaces) {
                customDetectedFaces.add(new CustomDetectedFace(df));
            }

            // drawing rectangle around the first face
            drawRectangleOnImage(0);

            // setting the description to the first element
            setDescriptionFromList(0);
        } else {
            setDescription("Nem található felismerhető arc a képen.");
        }
        return null;
    }

    @FXML
    public void onWebcamAnalyzeButtonPressed(ActionEvent actionEvent) {
        onAnalyzeButtonPressed(actionEvent);
    }


    @FXML
    private void onRightArrowClick(MouseEvent mouseEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(customDetectedFaces.size() > 1) {
                    if (actualDisplayedRecord == customDetectedFaces.size() - 1) {
                        actualDisplayedRecord = 0;
                    } else {
                        actualDisplayedRecord++;
                    }
                    drawRectangleOnImage(actualDisplayedRecord);
                    setDescriptionFromList(actualDisplayedRecord);
                }
                return null;
            }
        };

        Thread t = new Thread(task);
        t.start();
    }


    @FXML
    private void onLeftArrowClick(MouseEvent mouseEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (customDetectedFaces.size() > 1) {
                    if (actualDisplayedRecord == 0) {
                        actualDisplayedRecord = customDetectedFaces.size() - 1;
                    } else {
                        actualDisplayedRecord--;
                    }
                    drawRectangleOnImage(actualDisplayedRecord);
                    setDescriptionFromList(actualDisplayedRecord);
                }
                return null;
            }
        };

        Thread t = new Thread(task);
        t.start();

    }
}
