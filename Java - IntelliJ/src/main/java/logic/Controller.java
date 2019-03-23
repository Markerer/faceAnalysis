package logic;

import java.io.File;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

import wrappers.CustomDetectedFace;
import wrappers.CustomVerifyResult;

public class Controller {

	RequestHandler rh;
	
	public Controller(RequestHandler rh) {
		this.rh = rh;
	}

	public List<DetectedFace> AnalyseLocalPicture(File image) {
		String json = rh.buildAndSendHttpRequestFromLocalContent(image);
		List<DetectedFace> detectedFaces = rh.getDetectedFacesFromJson(json);

		return detectedFaces;
	}
	
	public String CompareTwoPictures(File image1, File image2) {
		String faceId1 = "";
		String faceId2 = "";

		String json = rh.buildAndSendHttpRequestFromLocalContent(image1);
		List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
		if(detectedFace.size() > 0) {
			faceId1 = detectedFace.get(0).faceId().toString();
			System.out.println(faceId1);

			json = rh.buildAndSendHttpRequestFromLocalContent(image2);
			detectedFace = rh.getDetectedFacesFromJson(json);
			if (detectedFace.size() > 0) {
				faceId2 = detectedFace.get(0).faceId().toString();
				System.out.println(faceId2);

				String verifyJson = rh.sendVerifyRequest(faceId1, faceId2);
				VerifyResult result = rh.getVerifyResultFromJson(verifyJson);
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
		String json = rh.buildAndSendHttpRequestFromURL(url);
		List<DetectedFace> detectedFaces = rh.getDetectedFacesFromJson(json);

		return detectedFaces;
	}
}
