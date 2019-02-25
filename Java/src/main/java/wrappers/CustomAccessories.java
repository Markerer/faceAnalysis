package wrappers;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Accessory;

public class CustomAccessories {

	List<Accessory> accessories;

	public CustomAccessories(List<Accessory> accessories) {
		this.accessories = new ArrayList<Accessory>();
		this.accessories = accessories;
	}

	public String toString() {
		if (accessories.size() > 0) {
			String ret = "Fejviselet: ";
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("headWear")) {
					if (a.confidence() > 0.75) {
						ret += "Van";
					} else {
						ret += "Nincs";
					}
				}
			}
			ret += "\n";
			ret += "Szemüveg: ";
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("glasses")) {
					if (a.confidence() > 0.75) {
						ret += "Van";
					} else {
						ret += "Nincs";
					}
				}
			}
			ret += "\n";
			ret += "Maszk: ";
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("mask")) {
					if (a.confidence() > 0.75) {
						ret += "Van";
					} else {
						ret += "Nincs";
					}
				}
			}
			ret += "\n";

			return ret;
		}
		return "";
	}

}
