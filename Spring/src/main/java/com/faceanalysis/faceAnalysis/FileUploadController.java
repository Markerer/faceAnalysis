package com.faceanalysis.faceAnalysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@Controller
public class FileUploadController {

    private final StorageService storageService;
    private String lastFace;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        storageService.changeRootLocation("upload-dir");
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        model.addAttribute("face", lastFace);
        return "uploadForm";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = GET)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        storageService.changeRootLocation("upload-dir");
        Resource file = storageService.loadAsResource(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "image/png");
        return new ResponseEntity<>(file, headers, HttpStatus.OK);

        /*return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);*/
    }

    @RequestMapping(value = "/", method = POST, produces = "plain/text")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        String type = contentType.split("/")[0];
        if(type.equals("image")) {
            storageService.changeRootLocation("upload-dir");
            storageService.store(file);

            lastFace = BasicMethods.RunFaceAnalysis(file.getOriginalFilename(), false);  //elmentem egy tagváltozóba

            return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            return ResponseEntity.badRequest().body("Invalid image file!");
        }
    }

    @RequestMapping(value = "/files", method = GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> listUploadedFiles() throws IOException {
        storageService.changeRootLocation("upload-dir");

        List<String> locations = new ArrayList<>(storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        String json = new ObjectMapper().writeValueAsString(locations);

        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = "/files", method = DELETE, produces = "plain/text")
    public ResponseEntity<String> deleteUploadedFile(@RequestParam("filename") String filename){
        storageService.changeRootLocation("upload-dir");
        String response = storageService.deleteOne(filename);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/files/all", method = DELETE, produces = "plain/text")
    public ResponseEntity<String> deleteAllUploadedFiles(){
        storageService.changeRootLocation("upload-dir");
        storageService.deleteAll();
        return ResponseEntity.ok("Success");
    }



//---------------------------------------------------------------admin things-----------------------------------------------------------------------

    @RequestMapping(value = "/admin", method = POST, produces = "plain/text")
    public ResponseEntity<String> handleAdminUpload(@RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        String type = contentType.split("/")[0];
        if(type.equals("image")) {
            storageService.changeRootLocation("admin-upload-dir");
            storageService.store(file);

            lastFace = BasicMethods.RunFaceAnalysis(file.getOriginalFilename(), false);  //elmentem egy tagváltozóba

            return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            return ResponseEntity.badRequest().body("Invalid image file!");
        }
    }

    @RequestMapping(value = "/files/admin/{filename:.+}", method = GET, produces = "image/png")
    @ResponseBody
    public ResponseEntity<Resource> serveAdminFile(@PathVariable String filename) {
        storageService.changeRootLocation("admin-upload-dir");
        Resource file = storageService.loadAsResource(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "image/png");
        return new ResponseEntity<>(file, headers, HttpStatus.OK);


        /*return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);*/
    }

    @RequestMapping(value = "/files/admin", method = GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> listUploadedAdminFiles() throws IOException {
        storageService.changeRootLocation("admin-upload-dir");

        List<String> locations = new ArrayList<>(storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveAdminFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        String json = new ObjectMapper().writeValueAsString(locations);

        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = "/files/admin", method = DELETE, produces = "plain/text")
    public ResponseEntity<String> deleteUploadedAdminFile(@RequestParam("filename") String filename){
        storageService.changeRootLocation("admin-upload-dir");
        String response = storageService.deleteOne(filename);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/files/admin/all", method = DELETE, produces = "plain/text")
    public ResponseEntity<String> deleteAllUploadedAdminFiles(){
        storageService.changeRootLocation("admin-upload-dir");
        storageService.deleteAll();
        return ResponseEntity.ok("Success");
    }



    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}