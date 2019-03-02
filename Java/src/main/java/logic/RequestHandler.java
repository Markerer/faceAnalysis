package logic;

import java.io.File;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import exception.RequestFailedException;

public class RequestHandler {
	private static final String subscriptionKey = "fc16ba16e4b4499a9cfbb4802a497e9e";

	private static final String uriBaseforLocalContent = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect?overload=stream&";

	private static final String uriBaseforURL = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

	private static final String imageWithFaces = "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

	private static final String faceAttributes = "age,gender,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories";

	private File image;

	public RequestHandler() {
		
	}

	// Ez majd arra az eshetőségre, ha URL-ből is akarunk képet beolvasni...
	@SuppressWarnings("unused")
	private DetectedFace buildAndSendHttpRequestFromURL() {
		HttpClient httpclient = HttpClientBuilder.create().build();
		System.out.println("fut");

		try {
			URIBuilder builder = new URIBuilder(uriBaseforURL);

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
			if (image != null) {
				request.setEntity(reqEntity);
			}

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				DetectedFace detectedFace = new DetectedFace();
				ObjectMapper mapper = new ObjectMapper();

				// Format and display the JSON response.
				System.out.println("REST Response:\n");

				String jsonString = EntityUtils.toString(entity).trim();
				// amennyiben ezzel kezdõdik, akkor az tömb...
				if (jsonString.charAt(0) == '[') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));

					// Így egy rendes objektumba rakhatjuk...
					detectedFace = mapper.readValue(jsonString, DetectedFace.class);
					// TODO Controller osztályon keresztül átadni
					return detectedFace;
				}
				// amennyiben ezzel kezdõdik, akkor az csak egy objektum...
				else if (jsonString.charAt(0) == '{') {
					detectedFace = mapper.readValue(jsonString, DetectedFace.class);
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));
		         // TODO Controller osztályon keresztül átadni
					return detectedFace;
					
				} else {
					System.out.println(jsonString);
					// TODO Controller osztályon keresztül átadni
					
				}
			}
			throw new RequestFailedException();
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
			return null;
		}
	}

	// Ez arra az esetre, ha beolvasott képes kérést küldünk.
	private DetectedFace buildAndSendHttpRequestFromLocalContent() {
		HttpClient httpclient = HttpClientBuilder.create().build();
		System.out.println("fut");

		try {
			URIBuilder builder = new URIBuilder(uriBaseforLocalContent);

			// Request parameters. All of them are optional.
			builder.setParameter("returnFaceId", "true");
			builder.setParameter("returnFaceLandmarks", "false");
			builder.setParameter("returnFaceAttributes", faceAttributes);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/octet-stream");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body.
			// StringEntity reqEntity = new StringEntity(imageWithFaces);
			if (image != null) {
				System.out.println("kep");
				FileEntity fileEntity = new FileEntity(image, ContentType.APPLICATION_OCTET_STREAM);
				request.setEntity(fileEntity);
			}

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				DetectedFace detectedFace = new DetectedFace();
				ObjectMapper mapper = new ObjectMapper();


				String jsonString = EntityUtils.toString(entity).trim();
				// amennyiben ezzel kezdõdik, akkor az tömb...
				
				//TODO: megcsinálni, hogy több arcot felismerjen
				if (jsonString.charAt(0) == '[') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));

					// így egy rendes objektumba rakhatjuk...
					detectedFace = mapper.readValue(jsonString, DetectedFace.class);
					// TODO Controller osztályon keresztül átadni
					return detectedFace;
				}
				// amennyiben ezzel kezdõdik, akkor az csak egy objektum...
				else if (jsonString.charAt(0) == '{') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));
					
					
					detectedFace = mapper.readValue(jsonString, DetectedFace.class);
					// TODO Controller osztályon keresztül átadni
					return detectedFace;
					
				} else {
					System.out.println(jsonString);
					// TODO Controller osztályon keresztül átadni
					
				}
			}
			throw new RequestFailedException();
		}
		catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
			return null;
		}
	}

	
	// Controller osztály hívná ezt meg...
	public void imageAdded(File image) {
		this.image = image;
	}

	// Controller osztály hívná meg...
	public DetectedFace requestButtonPressed() {
		if (image != null) {
			return buildAndSendHttpRequestFromLocalContent();
		}
		return null; // csúnya, de most fáradt vagyok xd
	}

}
