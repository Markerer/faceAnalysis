package faceAnalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;
import logic.Controller;
import logic.RequestHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import view.UI;
import org.apache.commons.io.FileUtils;
import wrappers.CustomDetectedFace;
import wrappers.CustomVerifyResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws JsonProcessingException {

        if (args.length == 0 || args.length > 3) {
            UI ui = new UI();
            ui.go();
        }

        RequestHandler rh = new RequestHandler();
        Controller controller = new Controller(rh);

        ObjectMapper om = new ObjectMapper();
        om.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        // if you want to prevent an exception when classes have no annotated properties
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        if (args.length == 2) {
            // amennyiben az eredeti json kell
            if (args[1].equals("original")) {
                if (args[0].contains("http")) {
                    System.out.println(rh.buildAndSendHttpRequestFromURL(args[0]));
                } else {
                    System.out.println(rh.buildAndSendHttpRequestFromLocalContent(new File(args[0])));
                }
            }
            // amennyiben a feldolgozott
            if (args[1].equals("processed")) {
                List<DetectedFace> list;
                if (args[0].contains("http")) {
                    list = controller.AnalysePictureURL(args[0]);
                } else {
                    list = controller.AnalyseLocalPicture(new File(args[0]));
                }
                List<CustomDetectedFace> customList = new ArrayList<>();
                for (DetectedFace df : list) {
                    customList.add(new CustomDetectedFace(df));
                }

                String jsonTry = om.writeValueAsString(customList);
                System.out.println(jsonTry);
            }
        } else if (args.length == 3) {
            String file1Id = "";
            String file2Id = "";

            if (args[0].contains("http")) {
                String json = rh.buildAndSendHttpRequestFromURL(args[0]);
                List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                if (detectedFace.size() > 0) {
                    file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                }
                // a reguláris kifejezés ellenőrzi, hogy a kiterjesztés megfelelő-e
            } else if (args[0].matches("([^\\s]+(\\.(?i)(jpg|png|jpeg|bmp))$)")) {
                String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[0]));
                List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                if (detectedFace.size() > 0) {
                    file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                }
            }
            // egyébként pedig már kész ID-ként dolgozzuk fel.
            else {
                file1Id = args[0];
            }
            if (args[1].contains("http")) {
                String json = rh.buildAndSendHttpRequestFromURL(args[1]);
                List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                if (detectedFace.size() > 0) {
                    file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                }
            } // a reguláris kifejezés ellenőrzi, hogy a kiterjesztés megfelelő-e
            else if (args[1].matches("([^\\s]+(\\.(?i)(jpg|png|jpeg|bmp))$)")) {
                String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[1]));
                List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                if (detectedFace.size() > 0) {
                    file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                }
            }
            // egyébként pedig már kész ID-ként dolgozzuk fel.
            else {
                file2Id = args[1];
            }
            if (!file1Id.equals("")) {
                if (!file2Id.equals("")) {
                    // amennyiben az eredeti json kell
                    if (args[2].equals("original")) {
                        System.out.println(rh.sendVerifyRequest(file1Id, file2Id));
                    }
                    // amennyiben a feldolgozott
                    if (args[2].equals("processed")) {
                        String json = rh.sendVerifyRequest(file1Id, file2Id);
                        VerifyResult result = rh.getVerifyResultFromJson(json);
                        CustomVerifyResult customVerifyResult = new CustomVerifyResult(result);

                        String jsonTry = om.writeValueAsString(customVerifyResult);
                        System.out.println(jsonTry);
                    }
                } else {
                    System.out.println("A jobb oldali képen nem található felismerhető arc.");
                }
            } else {
                System.out.println("A bal oldali képen nem található felismerhető arc.");
            }
        } else
            System.out.println("Valami probléma volt, írj a főnöknek.");
    }

}
