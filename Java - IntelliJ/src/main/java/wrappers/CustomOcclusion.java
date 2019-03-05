package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Occlusion;

public class CustomOcclusion {

	Occlusion occlusion;
	
	
	public CustomOcclusion(Occlusion occlusion) {
		this.occlusion = occlusion;
	}
	
	public String toString() {
		String ret = "Homlok eltakarva: ";
		if(occlusion.foreheadOccluded()) {
			ret += "Igen";
		} else {
			ret += "Nem";
		}
		ret += "\n";
		ret += "Szem eltakarva: ";
		if(occlusion.eyeOccluded()) {
			ret += "Igen";
		} else {
			ret += "Nem";
		}
		ret += "\n";
		ret += "Száj eltakarva: ";
		if(occlusion.mouthOccluded()) {
			ret += "Igen";
		} else {
			ret += "Nem";
		}
		ret += "\n";
		
		return ret;
	}
}
