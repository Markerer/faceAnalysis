package wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Hair;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.HairColor;

public class CustomHair {

	Hair hair;

	public CustomHair(Hair hair) {

		this.hair = hair;
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

		List<CustomHairColor> customHairColors = new ArrayList<CustomHairColor>();
		
		for (HairColor hc : hair.hairColor()) {
			customHairColors.add(new CustomHairColor(hc));
		}
		Collections.sort(customHairColors);
		
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
