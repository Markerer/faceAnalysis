package logic;

import java.io.File;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

import wrappers.CustomVerifyResult;

public class Controller {

	RequestHandler rh;
	
	public Controller(RequestHandler rh) {
		this.rh = rh;
	}

	public List<DetectedFace> AnalyseLocalPicture(File image) {
		List<DetectedFace> detectedFaces = rh.buildAndSendHttpRequestFromLocalContent(image);

		return detectedFaces;
	}
	
	public String CompareTwoPictures(File image1, File image2) {
		String faceId1 = "";
		String faceId2 = "";
		
		List<DetectedFace> detectedFace = rh.buildAndSendHttpRequestFromLocalContent(image1);
		if(detectedFace.size() > 0) {
			faceId1 = detectedFace.get(0).faceId().toString();
			System.out.println(faceId1);

			detectedFace = rh.buildAndSendHttpRequestFromLocalContent(image2);
			if (detectedFace.size() > 0) {
				faceId2 = detectedFace.get(0).faceId().toString();
				System.out.println(faceId2);

				VerifyResult result = rh.sendVerifyRequest(faceId1, faceId2);
				CustomVerifyResult customVerifyResult = new CustomVerifyResult(result);
				return customVerifyResult.toString();
			} else {
				return "Nem található arc a jobb oldali képen.";
			}

		} else {
			return "Nem található arc a bal oldali képen.";
		}
	}
	
	public List<DetectedFace> AnalysePictureURL(String url) {
		List<DetectedFace> detectedFaces = rh.buildAndSendHttpRequestFromURL(url);

		return detectedFaces;
	}
}
