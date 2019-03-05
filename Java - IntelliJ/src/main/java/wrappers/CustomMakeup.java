package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Makeup;

public class CustomMakeup {

	Makeup makeup;
	
	public CustomMakeup(Makeup makeup) {
		this.makeup = makeup;
	}
	
	public String toString() {
		String ret = "Szem smink: ";
		if(makeup.eyeMakeup()) {
			ret += "Van";
		} else {
			ret += "Nincs";
		}
		ret += "\n";
		ret += "Ajak smink: ";
		if(makeup.lipMakeup()) {
			ret += "Van";
		} else {
			ret += "Nincs";
		}
		ret += "\n";
		
		return ret;
	}
	
}
