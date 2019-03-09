package fxmlController;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FaceRectangle;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.UI;
import wrappers.CustomDetectedFace;

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

    private List<CustomDetectedFace> customDetectedFaces;

    private File imageFile;

    @FXML
    private MenuController menuController;

    @FXML
    private Text description;

    @FXML
    private ImageView image;

    @FXML
    private Button analysisButton;

    @FXML
    private Button imageChooserButton;

    private int actualDisplayedRecord = 0;

    @FXML
    private void onScroll(final ScrollEvent event) {
        if(customDetectedFaces.size() > 1) {
            // if the scroll was upwards
            if (event.getDeltaY() > 0.0) {
                if (actualDisplayedRecord == 0) {
                    actualDisplayedRecord = customDetectedFaces.size() - 1;
                } else {
                    actualDisplayedRecord--;
                }
            }
            // if the scroll was downwards
            if (event.getDeltaY() < 0.0) {
                if (actualDisplayedRecord == customDetectedFaces.size() - 1) {
                    actualDisplayedRecord = 0;
                } else {
                    actualDisplayedRecord++;
                }
            }

            drawRectangleOnImage(actualDisplayedRecord);
            setDescriptionFromList(actualDisplayedRecord);
        }
    }

    @FXML
    private void onImageChooserButtonPressed(final ActionEvent event) {
        File file = fileChooserDialog();
        if (file != null) {
            this.imageFile = file;
            Image image = new Image(file.toURI().toString());
            this.image.setImage(image);
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
        // interfészes cuccos, ehelyett Controlleren keresztül hívás
        // Listát kapunk vissza, jelenleg csak az 1. elemmel foglalkozunk
        // TODO bővítési lehetőség (több arc tulajdonságának kiírása)
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                List<DetectedFace> detectedFaces = UI.controller.AnalyseLocalPicture(imageFile);

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
        };

        Thread t = new Thread(task);
        t.start();

    }

    public void setDescription(String message){
        description.setText(message);
    }

    public void setDescriptionFromList(int numOfRecord) {
        if(this.customDetectedFaces.size() > numOfRecord){
            description.setText(customDetectedFaces.get(numOfRecord).toString());
        }
    }


    public void setStage(final Stage stage) {
        this.menuController.setStage(stage);
    }

    // hivatalos inicializáló whatever, lehet nem is kellene...
    public void initialize(URL location, ResourceBundle resources) {

        customDetectedFaces = new ArrayList<>();
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
                    image.setImage(temp);
                    return null;
                }
            };

            Thread t = new Thread(task);
            t.start();
        }
    }

}
