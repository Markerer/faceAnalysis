package faceAnalysis;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.DetectedFace;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.VerifyResult;
import logic.Controller;
import logic.RequestHandler;
import org.json.JSONArray;
import view.UI;
import org.apache.commons.io.FileUtils;
import wrappers.CustomVerifyResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {


	public static void main(String[] args) {

		if (args.length == 0 || args.length > 3 ) {
            UI ui = new UI();
            ui.go();
        }

        RequestHandler rh = new RequestHandler();
		Controller controller = new Controller(rh);

        if (args.length == 2) {
            // amennyiben az eredeti json kell
            if(args[1].equals("original")) {
                if (args[0].contains("http")) {
                    System.out.println(rh.buildAndSendHttpRequestFromURL(args[0]));
                } else {
                    System.out.println(rh.buildAndSendHttpRequestFromLocalContent(new File(args[0])));
                }
            }
            // amennyiben a feldolgozott
            if(args[1].equals("processed")){
                if (args[0].contains("http")) {
                    List<DetectedFace> list = controller.AnalysePictureURL(args[0]);
                    JSONArray jsonArray = new JSONArray(list);
                    System.out.println(jsonArray.toString());
                } else {
                    List<DetectedFace> list = controller.AnalyseLocalPicture(new File(args[0]));
                    JSONArray jsonArray = new JSONArray(list);
                    System.out.println(jsonArray.toString());
                }
            }
        }
        else if (args.length == 3) {
            String file1Id = "";
            String file2Id = "";

                if (args[0].contains("http")) {
                    String json = rh.buildAndSendHttpRequestFromURL(args[0]);
                    List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                    if(detectedFace.size() > 0) {
                        file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                    }
                } else {
                    String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[0]));
                    List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                    if(detectedFace.size() > 0) {
                        file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                    }
                }
                if (args[1].contains("http")) {
                    String json = rh.buildAndSendHttpRequestFromURL(args[1]);
                    List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                    if(detectedFace.size() > 0) {
                        file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                    }
                } else {
                    String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[1]));
                    List<DetectedFace> detectedFace = rh.getDetectedFacesFromJson(json);
                    if(detectedFace.size() > 0) {
                        file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
                    }
                }
                if(!file1Id.equals("")) {
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
                            System.out.println(customVerifyResult.toString());
                        }
                    } else {
                        System.out.println("A jobb oldali képen nem található felismerhető arc.");
                    }
                } else {
                    System.out.println("A bal oldali képen nem található felismerhető arc.");
                }
        }
        else
            System.out.println("Valami probléma volt, írj a főnöknek.");
    }

}
