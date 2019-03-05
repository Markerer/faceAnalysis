package wrappers;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

public class CustomVerifyResult {

	
	private VerifyResult verifyResult;
	
	
	public CustomVerifyResult(VerifyResult verifyResult) {
		this.verifyResult = verifyResult;
	}
	
	public String toString() {
		if(this.verifyResult != null) {
		String ret = "Megegyezik a két arc: ";
		
		if(this.verifyResult.isIdentical()) {
			ret += "Igen";
		} else {
			ret += "Nem";
		}
		ret += "\n";
		ret += "Egyezés foka: ";
		ret += this.verifyResult.confidence() + "\n";
		
		return ret;
		} else {
			return "";
		}
	}
	
	
}
