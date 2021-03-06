package logic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
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
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;

import exception.RequestFailedException;

public class RequestHandler {
    private static final String subscriptionKey = "fc16ba16e4b4499a9cfbb4802a497e9e";

    private static final String uriBaseforLocalContentDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect?overload=stream&";

    private static final String uriBaseforURLDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    private static final String uriBaseforVerify = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/verify";

    private static final String imageWithFaces = "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

    private static final String faceAttributes = "age,gender,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories";

    public RequestHandler() {

    }


    public String buildAndSendHttpRequestFromURL(String url) {

        url = "{\"url\":\"" + url + "\"}";

        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(uriBaseforURLDetect);

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
            StringEntity reqEntity = new StringEntity(url);
            request.setEntity(reqEntity);


            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                return jsonString;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }

        return "";
    }

    // Ez arra az esetre, ha beolvasott képes kérést küldünk.
    public String buildAndSendHttpRequestFromLocalContent(File image) {

        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(uriBaseforLocalContentDetect);

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
                FileEntity fileEntity = new FileEntity(image, ContentType.APPLICATION_OCTET_STREAM);
                request.setEntity(fileEntity);
            }


            HttpResponse response = httpclient.execute(request);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                return jsonString;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String sendVerifyRequest(String faceId1, String faceId2) {

        VerifyResult vf = new VerifyResult();

        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(uriBaseforVerify);

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            JSONObject json = new JSONObject();
            json.put("faceId1", faceId1);
            json.put("faceId2", faceId2);
            StringEntity reqEntity = new StringEntity(json.toString());
            request.setEntity(reqEntity);


            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                return jsonString;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }


    public VerifyResult getVerifyResultFromJson(String json){
        VerifyResult vf = new VerifyResult();
            ObjectMapper mapper = new ObjectMapper();

            if (json.charAt(0) == '{') {

                VerifyResult temp = null;
                try {
                    temp = mapper.readValue(json, VerifyResult.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                vf.withConfidence(temp.confidence());
                vf.withIsIdentical(temp.isIdentical());


            } else {
                System.out.println(json);
            }

        return vf;
    }


    public List<DetectedFace> getDetectedFacesFromJson(String json){
        List<DetectedFace> detectedFaces = new ArrayList<DetectedFace>();
        ObjectMapper mapper = new ObjectMapper();
        // amennyiben ezzel kezdõdik, akkor az tömb...
        if (json != null) {
            if (json.charAt(0) == '[') {

                JSONArray jsonArray = new JSONArray(json);
                //System.out.println(jsonArray.toString(2));

                // így egy rendes objektumba rakhatjuk...

                try {
                    detectedFaces.addAll(mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, DetectedFace.class)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //System.out.println(jsonString);
            }
        }
        return detectedFaces;
    }


}
