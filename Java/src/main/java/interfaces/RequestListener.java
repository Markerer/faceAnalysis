package interfaces;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

public interface RequestListener {

	void requestFailed();
	
	void requestSuccess(DetectedFace detectedFace);
	
}
