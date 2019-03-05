package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.HairColor;

public class CustomHairColor implements Comparable<Object> {
	
	HairColor hairColor;
	
	public enum Color{
		BROWN ("Barna"),
		BLOND ("Szőke"),
		BLACK ("Fekete"),
		OTHER ("Egyéb"),
		UNKNOWN ("Ismeretlen"),
		WHITE ("Ősz"),
		GRAY ("Szürke"),
		RED ("Vörös");
		
		private final String color;
		Color(String color){
			this.color = color;
		}
	}
	
	public CustomHairColor(HairColor hairColor) {
		this.hairColor = hairColor;
	}

	public int compareTo(Object toCompare) {
		double compareConfidence = ((CustomHairColor)toCompare).hairColor.confidence();
		if (compareConfidence > this.hairColor.confidence()) {
			return 1;
		} else {
			return -1;
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
