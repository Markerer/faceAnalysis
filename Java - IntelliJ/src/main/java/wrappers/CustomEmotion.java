package wrappers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Emotion;

public class CustomEmotion {

	@JsonIgnore
	Emotion emotion;


	@JsonIgnore
	List<MyEmotion> myEmotions;

	
	public CustomEmotion(Emotion emotion) {
		this.emotion = emotion;
		myEmotions = new ArrayList<MyEmotion>();
		myEmotions.add(new MyEmotion("Düh", this.emotion.anger()));
		myEmotions.add(new MyEmotion("Megvetés", this.emotion.contempt()));
		myEmotions.add(new MyEmotion("Undor", this.emotion.disgust()));
		myEmotions.add(new MyEmotion("Félelem", this.emotion.fear()));
		myEmotions.add(new MyEmotion("Boldogság", this.emotion.happiness()));
		myEmotions.add(new MyEmotion("Semlegesség", this.emotion.neutral()));
		myEmotions.add(new MyEmotion("Szomorúság", this.emotion.sadness()));
		myEmotions.add(new MyEmotion("Meglepettség", this.emotion.surprise()));
		
		
	}
	
	public String toString() {
		
		String toReturn = "";
		
		for(MyEmotion me : myEmotions) {
			toReturn += me.toString();
		}
		
		return toReturn;
	}
	
}
