package com.faceanalysis.faceAnalysis;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class AnalysisRequestController {

    private final StorageService storageService;
    @Autowired
    public AnalysisRequestController(StorageService storageService) {
        this.storageService = storageService;
    }


    // use like http://localhost:8080/analysis?filename=face1.jpg
    @RequestMapping(value = "/analysis", method = GET, produces = "application/json")
    public ResponseEntity<String> getFaceAnalysis(@RequestParam("filename") String filename){
        File file = new File("upload-dir" +
                "/" + filename);
        if(file.exists() || filename.contains("http")) {
            String json = BasicMethods.RunFaceAnalysis(filename, true);
            return ResponseEntity.ok(json);
        }
        else {
            return ResponseEntity.badRequest().body("The given file doesn't exist on our server.");
        }
    }


    // use like http://localhost:8080/compare?filename1=face1.jpg&filename2=face2.jpg
    @RequestMapping(value = "/compare", method = GET, produces = "application/json")
    public ResponseEntity<String> getFaceComparison(@RequestParam("filename1") String filename1,
                                    @RequestParam("filename2") String filename2){
        File file1 = new File("upload-dir" + "/" + filename1);
        File file2 = new File("upload-dir" + "/" + filename2);

        if(file1.exists()) {

            if(file2.exists()) {
                String json = BasicMethods.RunFaceComparison(filename1, filename2, true);
                return ResponseEntity.ok(json);
            } else {
                return ResponseEntity.badRequest().body("The second given file doesn't exist on our server.");
            }
        } else {
            return ResponseEntity.badRequest().body("The first given file doesn't exist on our server.");
        }
    }


    @RequestMapping(value = "/compareAdmin", method = GET, produces = "application/json")
    public ResponseEntity<String> getAdminFaceComparion(@RequestParam("filename") String filename){
        File file1 = new File("admin-upload-dir" + "/" + filename);

        storageService.changeRootLocation("admin-upload-dir");

        double confidence = 0.0;
        String pathToMostSimilarImage = "";
        JSONObject mostSimilarJsonObject = new JSONObject();

        if(file1.exists()){
            List<String> locations = new ArrayList<>(storageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveAdminFile", path.getFileName().toString()).build().toString())
                    .collect(Collectors.toList()));

            for(String file : locations){

                String[] splitted = file.split("/");
                String compareTo = splitted[splitted.length - 1];

                if(!compareTo.equals(filename)) {
                    String json = BasicMethods.RunAdminFaceComparison(filename, compareTo, true);
                    JSONObject jsonObject = null;
                    if (!json.equals("") && json.contains("{")) {
                        try {
                            jsonObject = new JSONObject(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            double temp = (double) jsonObject.get("Confidence");
                            if (temp > confidence) {
                                confidence = temp;
                                pathToMostSimilarImage = file;
                                mostSimilarJsonObject = jsonObject;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            try {
                mostSimilarJsonObject.put("Image", pathToMostSimilarImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            storageService.deleteOne(filename);

            return ResponseEntity.ok(mostSimilarJsonObject.toString());


        } else {
            return ResponseEntity.badRequest().body("The given file doesn't exist on our server.");
        }
    }
}
