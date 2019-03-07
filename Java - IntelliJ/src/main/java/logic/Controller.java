package logic;

import java.io.File;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

import view.UI;
import wrappers.CustomVerifyResult;

public class Controller {

	RequestHandler rh;
	UI userinterface;
	
	public Controller(RequestHandler rh) {
		this.rh = rh;
	}
	public List<DetectedFace> AnalyseLocalPicture(File image) {
		List<DetectedFace> detectedFace = rh.buildAndSendHttpRequestFromLocalContent(image);

		return detectedFace;
	}
	
	public String CompareTwoPictures(File image1, File image2) {
		String faceId1 = "";
		String faceId2 = "";
		
		List<DetectedFace> detectedFace = rh.buildAndSendHttpRequestFromLocalContent(image1);
		if(detectedFace.size() > 0) {
			faceId1 = detectedFace.get(0).faceId().toString();			
			System.out.println(faceId1);
			
			detectedFace = rh.buildAndSendHttpRequestFromLocalContent(image2);
			if(detectedFace.size() > 0) {
				faceId2 = detectedFace.get(0).faceId().toString();
				System.out.println(faceId2);
				
				VerifyResult result = rh.sendVerifyRequest(faceId1, faceId2);
				CustomVerifyResult customVerifyResult = new CustomVerifyResult(result);
				return customVerifyResult.toString();
			}
		}
				
		return "";
		
	}
	
	public void AnalysePictureURL() {
		//TODO: megcsinálni ezt is
	}
}
