package com.faceanalysis.faceAnalysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


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

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        model.addAttribute("face", lastFace);
        return "uploadForm";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = GET, produces = "image/png")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/", method = POST, produces = "plain/text")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        String type = contentType.split("/")[0];
        if(type.equals("image")) {
            storageService.store(file);

            lastFace = BasicMethods.RunFaceAnalysis(file.getOriginalFilename(), true);  //elmentem egy tagváltozóba

            return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            return ResponseEntity.badRequest().body("Invalid image file!");
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}