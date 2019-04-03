package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

public class CustomVerifyResult {

	@JsonIgnore
	private VerifyResult verifyResult;

	@JsonProperty("IsIdentical")
	String IsIdentical;

	@JsonProperty("Confidence")
	Double Confidence;
	
	public CustomVerifyResult(VerifyResult verifyResult) {
		this.verifyResult = verifyResult;
		setValues();
	}

	private void setValues() {
		if (this.verifyResult != null) {

			if (this.verifyResult.isIdentical()) {
				IsIdentical = "Igen";
			} else {
				IsIdentical = "Nem";
			}
			Confidence = this.verifyResult.confidence();

		}
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
