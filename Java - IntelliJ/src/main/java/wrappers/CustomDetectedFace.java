package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FaceRectangle;

import java.util.ArrayList;
import java.util.List;


public class CustomDetectedFace {

	@JsonIgnore
	private DetectedFace detectedFace;

	@JsonIgnore
	private FaceRectangle faceRectangle;


	@JsonProperty("Accessories")
	List<String> customAccessoryList;

	@JsonProperty("Emotions")
	List<MyEmotion> customEmotionList;

	@JsonIgnore
	private CustomAccessories customAccessories;

	@JsonIgnore
	private CustomEmotion customEmotion;

	@JsonProperty("FacialHair")
	private CustomFacialHair customFacialHair;

	@JsonProperty("Hair")
	private CustomHair customHair;

	@JsonProperty("Makeup")
	private CustomMakeup customMakeup;

	@JsonProperty("Occlusion")
	private CustomOcclusion customOcclusion;


	@JsonProperty("Gender")
	String Gender;

	@JsonProperty("Smile")
	String Smile;

	@JsonProperty("Glasses")
	String Glasses;

	@JsonProperty("FaceRectangle")
	List<Integer> Rectangle;
	
	public CustomDetectedFace(DetectedFace detectedFace) {
		this.detectedFace = detectedFace;
		this.customAccessories = new CustomAccessories(this.detectedFace.faceAttributes().accessories());
		this.customEmotion = new CustomEmotion(this.detectedFace.faceAttributes().emotion());
		this.customFacialHair = new CustomFacialHair(this.detectedFace.faceAttributes().facialHair());
		this.customHair = new CustomHair(this.detectedFace.faceAttributes().hair());
		this.customMakeup = new CustomMakeup(this.detectedFace.faceAttributes().makeup());
		this.customOcclusion = new CustomOcclusion(this.detectedFace.faceAttributes().occlusion());
		this.faceRectangle = this.detectedFace.faceRectangle();
		Rectangle = new ArrayList<>();

		customAccessoryList = customAccessories.myAccessories;
		customEmotionList = customEmotion.myEmotions;
		setValues();
	}

	public void setValues(){
		String gender = detectedFace.faceAttributes().gender().name();
		if(gender.equals("FEMALE")){
			Gender = "Nő";
		}
		if(gender.equals("MALE")) {
			Gender = "Férfi";
		}
		if(gender.equals("GENDERLESS")) {
			Gender = "Bizonytalan";
		}

		double smile = detectedFace.faceAttributes().smile();
		if(smile < 0.2) {
			Smile = "Nincs";
		}
		if(smile > 0.2 && smile < 0.6) {
			Smile = "Minimális";
		}
		if(smile > 0.6) {
			Smile = "Nagy mosoly";
		}

		String glasses = detectedFace.faceAttributes().glasses().name();
		if(glasses.equals("NO_GLASSES")) {
			Glasses = "Nincs";
		}
		if(glasses.equals("READING_GLASSES")) {
			Glasses = "Olvasószemüveg";
		}
		if(glasses.equals("SUNGLASSES")) {
			Glasses = "Napszemüveg";
		}
		if(glasses.equals("SWIMMING_GOGGLES")) {
			Glasses = "Úszószemüveg";
		}

		Rectangle.add(this.faceRectangle.left());
		Rectangle.add(this.faceRectangle.top());
		Rectangle.add(this.faceRectangle.width());
		Rectangle.add(this.faceRectangle.height());
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
		ret += customAccessories.toString();		
		
		return ret;
	}

	public FaceRectangle getFaceRectangle(){

		return this.faceRectangle;
	}
	
}
