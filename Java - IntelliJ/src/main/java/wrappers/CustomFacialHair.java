package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FacialHair;

public class CustomFacialHair {

	FacialHair facialHair;
	
	public CustomFacialHair(FacialHair facialHair) {
		this.facialHair = facialHair;
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
