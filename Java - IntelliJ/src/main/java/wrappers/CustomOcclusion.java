package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Occlusion;

public class CustomOcclusion {

	@JsonIgnore
	Occlusion occlusion;

	@JsonProperty("ForeheadOccluded")
	String ForeheadOccluded;

	@JsonProperty("EyeOccluded")
	String EyeOccluded;

	@JsonProperty("MouthOccluded")
	String MouthOccluded;
	
	
	public CustomOcclusion(Occlusion occlusion) {
		this.occlusion = occlusion;
		setValues();
	}

	private void setValues(){
		if(occlusion.foreheadOccluded()) {
			ForeheadOccluded = "Igen";
		} else {
			ForeheadOccluded = "Nem";
		}
		if(occlusion.eyeOccluded()) {
			EyeOccluded = "Igen";
		} else {
			EyeOccluded = "Nem";
		}
		if(occlusion.mouthOccluded()) {
			MouthOccluded = "Igen";
		} else {
			MouthOccluded = "Nem";
		}
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
