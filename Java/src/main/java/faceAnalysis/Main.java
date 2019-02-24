package faceAnalysis;

import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

public class Main {

	private static final String subscriptionKey = "fc16ba16e4b4499a9cfbb4802a497e9e";

	private static final String uriBase = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

	private static final String imageWithFaces = "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

	private static final String faceAttributes = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

	public static void main(String[] args) {
		
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		try
		{
		    URIBuilder builder = new URIBuilder(uriBase);

		    // Request parameters. All of them are optional.
		    builder.setParameter("returnFaceId", "true");
		    builder.setParameter("returnFaceLandmarks", "false");
		    builder.setParameter("returnFaceAttributes", faceAttributes);

		    // Prepare the URI for the REST API call.
		    URI uri = builder.build();
		    HttpPost request = new HttpPost(uri);

		    // Request headers.
		    request.setHeader("Content-Type", "application/json");
		    request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

		    // Request body.
		    StringEntity reqEntity = new StringEntity(imageWithFaces);
		    request.setEntity(reqEntity);

		    // Execute the REST API call and get the response entity.
		    HttpResponse response = httpclient.execute(request);
		    HttpEntity entity = response.getEntity();
		    if (entity != null)
		    {
		    	DetectedFace[] detectedFaces = new DetectedFace[2];
		    	ObjectMapper mapper = new ObjectMapper();
		    	
		        // Format and display the JSON response.
		        System.out.println("REST Response:\n");
		        
		        String jsonString = EntityUtils.toString(entity).trim();
		        // amennyiben ezzel kezd�dik, akkor az t�mb...
		        if (jsonString.charAt(0) == '[') {
		            JSONArray jsonArray = new JSONArray(jsonString);
		            
		            // �gy egy rendes objektumba rakhatjuk...
		            detectedFaces = mapper.readValue(jsonString, DetectedFace[].class);
		           
		            System.out.println(jsonArray.toString(2));
		        }
		        // amennyiben ezzel kezd�dik, akkor az csak egy objektum...
		        else if (jsonString.charAt(0) == '{') {
		            JSONObject jsonObject = new JSONObject(jsonString);
		            detectedFaces[1] = mapper.readValue(jsonString, DetectedFace.class);
		            
		            
		            System.out.println(jsonObject.toString(2));
		        } else {
		            System.out.println(jsonString);
		        }
		        System.out.println(detectedFaces[0].faceAttributes().age() + "\t" + detectedFaces[0].faceRectangle().top());
		        
		    }
		}
		catch (Exception e)
		{
		    // Display error message.
		    System.out.println(e.getMessage());
		}
		
	}

}
