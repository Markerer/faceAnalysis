package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;

import interfaces.RequestListener;
import interfaces.UIListener;
import view.UI;

public class RequestHandler implements UIListener {
	private static final String subscriptionKey = "fc16ba16e4b4499a9cfbb4802a497e9e";

	private static final String uriBaseforLocalContent = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect?overload=stream&";

	private static final String uriBaseforURL = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

	private static final String imageWithFaces = "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

	private static final String faceAttributes = "age,gender,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories";

	private UI userInterface;

	private List<RequestListener> listeners;

	private File image;

	public RequestHandler() {
		userInterface = new UI();
		listeners = new ArrayList<RequestListener>();
	}

	public void start() {
		userInterface.go(this);
	}

	public void addListener(RequestListener toAdd) {
		System.out.println("requestlistener added");
		listeners.add(toAdd);
	}

	private void buildAndSendHttpRequestFromURL() {
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
				DetectedFace[] detectedFaces = new DetectedFace[2];
				ObjectMapper mapper = new ObjectMapper();

				// Format and display the JSON response.
				System.out.println("REST Response:\n");

				String jsonString = EntityUtils.toString(entity).trim();
				// amennyiben ezzel kezdõdik, akkor az tömb...
				if (jsonString.charAt(0) == '[') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));

					// így egy rendes objektumba rakhatjuk...
					detectedFaces = mapper.readValue(jsonString, DetectedFace[].class);
					for (RequestListener rh : listeners) {
						rh.requestSuccess(detectedFaces[0]);
					}
				}
				// amennyiben ezzel kezdõdik, akkor az csak egy objektum...
				else if (jsonString.charAt(0) == '{') {
					detectedFaces[1] = mapper.readValue(jsonString, DetectedFace.class);
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));
		            
					for (RequestListener rh : listeners) {
						rh.requestSuccess(detectedFaces[1]);
					}
				} else {
					System.out.println(jsonString);
					for (RequestListener rh : listeners) {
						rh.requestFailed();
					}
				}
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
	}

	private void buildAndSendHttpRequestFromLocalContent() {
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
				DetectedFace[] detectedFaces = new DetectedFace[2];
				ObjectMapper mapper = new ObjectMapper();


				String jsonString = EntityUtils.toString(entity).trim();
				// amennyiben ezzel kezdõdik, akkor az tömb...
				if (jsonString.charAt(0) == '[') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));

					// így egy rendes objektumba rakhatjuk...
					detectedFaces = mapper.readValue(jsonString, DetectedFace[].class);
					for (RequestListener rh : listeners) {
						rh.requestSuccess(detectedFaces[0]);
					}
				}
				// amennyiben ezzel kezdõdik, akkor az csak egy objektum...
				else if (jsonString.charAt(0) == '{') {
					
					JSONArray jsonArray = new JSONArray(jsonString);
		            System.out.println(jsonArray.toString(2));
					
					
					detectedFaces[1] = mapper.readValue(jsonString, DetectedFace.class);
					for (RequestListener rh : listeners) {
						rh.requestSuccess(detectedFaces[1]);
					}
				} else {
					System.out.println(jsonString);
					for (RequestListener rh : listeners) {
						rh.requestFailed();
					}
				}
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
	}

	public void imageAdded(File image) {

		this.image = image;
	}

	public void requestButtonPressed() {
		if (image != null) {
			buildAndSendHttpRequestFromLocalContent();
		}
	}

}
