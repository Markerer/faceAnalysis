package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.HairColor;

public class CustomHairColor implements Comparable<Object> {

	@JsonIgnore
	HairColor hairColor;

	@JsonIgnore
	String HairColor;


	public enum Color{
		BROWN ("Barna"),
		BLOND ("Szőke"),
		BLACK ("Fekete"),
		OTHER ("Egyéb"),
		UNKNOWN ("Ismeretlen"),
		WHITE ("Ősz"),
		GRAY ("Szürke"),
		RED ("Vörös");

		@JsonIgnore
		private final String color;
		Color(String color){
			this.color = color;
		}
	}
	
	public CustomHairColor(HairColor hairColor) {
		this.hairColor = hairColor;
		setValues();
	}

	public int compareTo(Object toCompare) {
		double compareConfidence = ((CustomHairColor)toCompare).hairColor.confidence();
		if (compareConfidence > this.hairColor.confidence()) {
			return 1;
		} else {
			return -1;
		}
	}

	private void setValues(){
		for (Color c : Color.values()) {
			if(c.name().equals(this.hairColor.color().name())) {
				HairColor = c.color;
				break;
			}
		}
	}
	
	
	public String toString() {
		for (Color c : Color.values()) {
			if(c.name().equals(this.hairColor.color().name())) {
				return c.color;
			}
		}
		return "Nem találtunk hajszínt.";
	}
	
}
