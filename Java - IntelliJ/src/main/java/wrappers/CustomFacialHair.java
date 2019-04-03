package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FacialHair;

public class CustomFacialHair {

	@JsonIgnore
	FacialHair facialHair;

	@JsonProperty("Moustache")
	String Moustache;

	@JsonProperty("Beard")
	String Beard;

	@JsonProperty("Sideburns")
	String Sideburns;
	
	public CustomFacialHair(FacialHair facialHair) {
		this.facialHair = facialHair;
		setValue();
	}


	private void setValue(){
		if(facialHair.moustache() > 0.75) {
			Moustache = "Van";
		} else {
			Moustache = "Nincs";
		}
		if(facialHair.beard() > 0.75) {
			Beard = "Van";
		} else {
			Beard = "Nincs";
		}
		if(facialHair.sideburns() > 0.75) {
			Sideburns = "Van";
		} else {
			Sideburns = "Nincs";
		}
	}

	public String toString() {
		String ret = "Bajusz: ";
		if(facialHair.moustache() > 0.75) {
			ret += "Van";
		} else {
			ret += "Nincs";
		} 
		ret += "\n";
		ret += "Szakáll: ";
		if(facialHair.beard() > 0.75) {
			ret += "Van";
		} else {
			ret += "Nincs";
		} 
		ret += "\n";
		ret += "Oldalszakáll: ";
		if(facialHair.sideburns() > 0.75) {
			ret += "Van";
		} else {
			ret += "Nincs";
		} 
		ret += "\n";
		
		return ret;
	}
}
