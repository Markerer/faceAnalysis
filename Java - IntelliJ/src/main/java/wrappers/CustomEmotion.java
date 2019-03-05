package wrappers;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Emotion;

public class CustomEmotion {

	Emotion emotion;
	List<MyEmotion> myEmotions;
	
	class MyEmotion{
		String name;
		double value;
		
		public MyEmotion(String name, double value) {
			this.name = name;
			this.value = value;
		}
		
		public String toString() {
			String ret = name + ": ";
			if(value < 0.2) {
				ret += "Nincs nyoma";
			}
			if(value > 0.2 && value < 0.5) {
				ret += "Minimálisan fellelhető";
			} 
			if(value > 0.5 && value < 0.75) {
				ret += "Észlelhető";
			}
			if(value > 0.75) {
				ret += "Egyértelműen";
			}
			ret += "\n";
			return ret;
		}
	}
	
	
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
