package logic;

import java.io.File;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import view.UI;

public class Controller {

	RequestHandler rh;
	UI userinterface;
	
	public Controller(RequestHandler rh) {
		this.rh = rh;
	}
	public DetectedFace AnalyseLocalPicture(File image) {
		rh.imageAdded(image);
		DetectedFace detectedFace = rh.requestButtonPressed();		
		
		return detectedFace;
	}
	
	public void CompareToPictures(File image1, File image2) {
		
	}
	
	public void AnalysePictureURL() {
		//TODO: megcsinálni ezt is
	}
}
