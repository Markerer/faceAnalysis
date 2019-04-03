package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Makeup;

public class CustomMakeup {

	@JsonIgnore
	Makeup makeup;

	@JsonProperty("EyeMakeup")
	String EyeMakeup;

	@JsonProperty("LipMakeup")
	String LipMakeup;
	
	public CustomMakeup(Makeup makeup) {
		this.makeup = makeup;
		setValues();
	}

	private void setValues(){
		if(makeup.eyeMakeup()) {
			EyeMakeup = "Van";
		} else {
			EyeMakeup = "Nincs";
		}
		if(makeup.lipMakeup()) {
			LipMakeup = "Van";
		} else {
			LipMakeup = "Nincs";
		}
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
