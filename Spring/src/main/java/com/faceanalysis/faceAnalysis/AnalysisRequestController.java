package com.faceanalysis.faceAnalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

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
        if(file.exists()) {
            String json = BasicMethods.RunFaceAnalysis(filename);
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
                String json = BasicMethods.RunFaceComparison(filename1, filename2);
                return ResponseEntity.ok(json);
            } else {
                return ResponseEntity.badRequest().body("The second given file doesn't exist on our server.");
            }
        } else {
            return ResponseEntity.badRequest().body("The first given file doesn't exist on our server.");
        }
    }
}
