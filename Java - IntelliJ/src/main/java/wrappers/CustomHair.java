package wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Hair;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.HairColor;

public class CustomHair {

	@JsonIgnore
	Hair hair;

	@JsonIgnore
	List<CustomHairColor> customHairColors;

	@JsonProperty
	String HairColor;

	@JsonProperty("IsInvisible")
	String IsInvisible;

	@JsonProperty("IsBald")
	String IsBald;

	public CustomHair(Hair hair) {

		this.hair = hair;
		customHairColors = new ArrayList<>();
		for(HairColor hc : hair.hairColor()){
			customHairColors.add(new CustomHairColor(hc));
		}
		Collections.sort(customHairColors);
		HairColor = customHairColors.get(0).HairColor;
		setValues();
	}

	private void setValues(){
			if (!hair.invisible()) {
				IsInvisible = "Nem";
			} else {
				IsInvisible = "Igen";
			}


			if (hair.bald() > 0.75) {
				IsBald = "Igen";
			} else {
				IsBald = "Nem";
			}
	}


	public String isVisible() {
		if (!hair.invisible()) {
			return "Igen";
		} else {
			return "Nem";
		}
	}

	public String isBald() {
		if (hair.bald() > 0.75) {
			return "Igen";
		} else {
			return "Nem";
		}
	}

	public String getHairColor() {
		
		String hairColors = "";

		hairColors += customHairColors.get(0).toString();
		
		/*if (customHairColors.get(1).hairColor.confidence() > 0.6) {
			hairColors += ", illetve";
			hairColors += customHairColors.get(1).toString();
		}*/

		return hairColors;
	}

	public String toString() {
		String ret = "";
		ret += "Kopasz: " + isBald() + "\n";
		ret += "Látható a haj: " + isVisible() + "\n";
		ret += "Hajszín: " + getHairColor() + "\n";
		return ret;
	}

}
