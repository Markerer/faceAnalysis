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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public ResponseEntity<String> getFaceAnalysis(@RequestParam("filename") String filename) {
        File file = new File("upload-dir" +
                "/" + filename);
        if (file.exists() || filename.contains("http")) {
            String json = BasicMethods.RunFaceAnalysis(filename, true);
            // amennyiben lekérik, és nem tartalmaz egy arcot sem, akkor töröljük
            if(!json.contains("{")){
                storageService.changeRootLocation("upload-dir");
                storageService.deleteOne(filename);
            }
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.badRequest().body("The given file doesn't exist on our server.");
        }
    }


    // use like http://localhost:8080/compare?filename1=face1.jpg&filename2=face2.jpg
    @RequestMapping(value = "/compare", method = GET, produces = "application/json")
    public ResponseEntity<String> getFaceComparison(@RequestParam("filename1") String filename1,
                                                    @RequestParam("filename2") String filename2) {
        File file1 = new File("upload-dir" + "/" + filename1);
        File file2 = new File("upload-dir" + "/" + filename2);

        if (file1.exists()) {

            if (file2.exists()) {
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
    public ResponseEntity<String> getAdminFaceComparion(@RequestParam("filename") String filename) throws JSONException {
        File file1 = new File("admin-upload-dir" + "/" + filename);

        storageService.changeRootLocation("admin-upload-dir");

        double confidence = 0.0;
        String pathToMostSimilarImage = "";
        JSONObject mostSimilarJsonObject = new JSONObject();

        ArrayList<String> responses = new ArrayList<>();
        ArrayList<String> fileLocations = new ArrayList<>();

        if (file1.exists()) {

            String faces = BasicMethods.RunFaceAnalysis(filename, true);
            if(faces.contains("{")) {

                List<String> locations = new ArrayList<>(storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveAdminFile", path.getFileName().toString()).build().toString())
                        .collect(Collectors.toList()));

                ExecutorService executor = Executors.newFixedThreadPool(15);

                for (String file : locations) {
                    executor.submit(() -> {
                        String[] splitted = file.split("/");
                        String compareTo = splitted[splitted.length - 1];

                        if (!compareTo.equals(filename)) {
                            String json = BasicMethods.RunAdminFaceComparison(filename, compareTo, true);
                            if (!json.equals("") && json.contains("{")) {
                                responses.add(json);
                                fileLocations.add(file);
                            }
                        }
                    });
                }

                awaitTerminationAfterShutdown(executor);


                for (int i = 0; i < responses.size(); i++) {
                    String result = responses.get(i);
                    JSONObject jsonObject = new JSONObject(result);

                    double temp = (double) jsonObject.get("Confidence");
                    if (temp > confidence) {
                        confidence = temp;
                        pathToMostSimilarImage = fileLocations.get(i);
                        mostSimilarJsonObject = jsonObject;
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
                storageService.deleteOne(filename);
                return ResponseEntity.badRequest().body("The given file doesn't contain any faces.");
            }

        } else {
            return ResponseEntity.badRequest().body("The given file doesn't exist on our server.");
        }
    }



    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
