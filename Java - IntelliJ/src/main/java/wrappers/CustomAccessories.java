package wrappers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Accessory;

public class CustomAccessories {

	@JsonIgnore
	List<Accessory> accessories;

	@JsonProperty("Accessories")
	List<String> myAccessories = new ArrayList<>();

	public CustomAccessories(List<Accessory> accessories) {
		this.accessories = new ArrayList<Accessory>();
		this.accessories.addAll(accessories);
		setValues();
	}

	private void setValues() {
		if (accessories.size() > 0) {
			for (Accessory a : this.accessories) {
				if (a.type().name().equals("headWear")) {
					if (a.confidence() > 0.75) {
						myAccessories.add("Fejviselet");
					}
				}
				if (a.type().name().equals("mask")) {
					if (a.confidence() > 0.75) {
						myAccessories.add("Maszk");
					}
				}
			}
		}
	}


	public String toString() {
		
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
