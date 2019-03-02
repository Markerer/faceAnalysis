package wrappers;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Accessory;

public class CustomAccessories {
	
	//TODO valami bug van, nullpointer... listával valami

	List<Accessory> accessories;

	public CustomAccessories(List<Accessory> accessories) {
		this.accessories = new ArrayList<Accessory>();
		this.accessories = accessories;
	}

	public String toString() {
		System.out.print(accessories.size());
		if (accessories.size() > 0) {
			String ret = "";
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("headWear")) {
					ret += "Fejviselet: ";
					if (a.confidence() > 0.75) {
						 ret += "Van\n";
					} else {
						ret += "Nincs\n";
					}
				}
			}
			/*
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("glasses")) {
					ret += "Szemüveg: ";
					if (a.confidence() > 0.75) {
						ret += "Van\n";
					} else {
						ret += "Nincs\n";
					}
				}
			}
			*/
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("mask")) {
					ret += "Maszk: ";
					if (a.confidence() > 0.75) {
						ret += "Van\n";
					} else {
						ret += "Nincs\n";
					}
				}
			}
			return ret;
		} else {
			return "";
		}
	}

}
