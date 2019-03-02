package logic;

import java.io.File;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import view.UI;

public class Controller {

	RequestHandler rh;
	UI userinterface;
	
	public Controller(RequestHandler rh) {
		this.rh = rh;
	}
	public List<DetectedFace> AnalyseLocalPicture(File image) {
		rh.imageAdded(image);
		List<DetectedFace> detectedFace = rh.requestButtonPressed();		
		
		return detectedFace;
	}
	
	public void CompareToPictures(File image1, File image2) {
		
	}
	
	public void AnalysePictureURL() {
		//TODO: megcsinálni ezt is
	}
}
