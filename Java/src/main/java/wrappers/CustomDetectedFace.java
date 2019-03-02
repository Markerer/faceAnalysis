package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;


public class CustomDetectedFace {
	
	private DetectedFace detectedFace;
	private CustomAccessories customAccessories;
	private CustomEmotion customEmotion;
	private CustomFacialHair customFacialHair;
	private CustomHair customHair;
	private CustomMakeup customMakeup;
	private CustomOcclusion customOcclusion;
	
	public CustomDetectedFace(DetectedFace detectedFace) {
		this.detectedFace = detectedFace;
		//this.customAccessories = new CustomAccessories(this.detectedFace.faceAttributes().accessories());
		this.customEmotion = new CustomEmotion(this.detectedFace.faceAttributes().emotion());
		this.customFacialHair = new CustomFacialHair(this.detectedFace.faceAttributes().facialHair());
		this.customHair = new CustomHair(this.detectedFace.faceAttributes().hair());
		this.customMakeup = new CustomMakeup(this.detectedFace.faceAttributes().makeup());
		this.customOcclusion = new CustomOcclusion(this.detectedFace.faceAttributes().occlusion());
	}
	
	public String getGender() {
		String ret = "";
		String gender = detectedFace.faceAttributes().gender().name();
		if(gender.equals("FEMALE")){
			ret += "Nő";
		}
		if(gender.equals("MALE")) {
			ret += "Férfi";
		}
		if(gender.equals("GENDERLESS")) {
			ret += "Bizonytalan";
		}
		ret += "\n";
		return ret;
	}
	
	public String getSmile() {
		String ret = "";
		double smile = detectedFace.faceAttributes().smile();
		if(smile < 0.2) {
			ret += "Nincs";
		}
		if(smile > 0.2 && smile < 0.6) {
			ret += "Minimális";
		}
		if(smile > 0.6) {
			ret += "Nagy mosoly";
		}
		ret += "\n";
		return ret;
	}
	
	public String getGlasses() {
		String ret = "";
		String glasses = detectedFace.faceAttributes().glasses().name();
		if(glasses.equals("NO_GLASSES")) {
			ret += "Nincs";
		}
		if(glasses.equals("READING_GLASSES")) {
			ret += "Olvasószemüveg";
		}
		if(glasses.equals("SUNGLASSES")) {
			ret += "Napszemüveg";
		}
		if(glasses.equals("SWIMMING_GOGGLES")) {
			ret += "Úszószemüveg";
		}
		ret += "\n";
		return ret;
	}
	
	public String toString() {
		String ret = "";
		ret += "Kor: " + detectedFace.faceAttributes().age() + "\n";
		
		ret += "Nem: " + getGender();
		
		ret += "Mosoly: " + getSmile();
		
		ret += customFacialHair.toString();
		
		ret += "Szemüveg: " + getGlasses();
		
		ret += customEmotion.toString();
		ret += customHair.toString();
		ret += customMakeup.toString();
		ret += customOcclusion.toString();
		//ret += customAccessories.toString();		
		
		return ret;
	}
	
	
}
