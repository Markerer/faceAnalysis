package interfaces;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

// TODO Törlendő 
public interface RequestListener {

	void requestFailed();
	
	void requestSuccess(DetectedFace detectedFace);
	
}
