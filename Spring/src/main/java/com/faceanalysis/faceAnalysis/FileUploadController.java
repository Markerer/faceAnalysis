package com.faceanalysis.faceAnalysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // Run a java app in a separate system process
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("java -jar faceAnalysis.jar upload-dir/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            proc.waitFor(); //bekell várni a jar file-t
        } catch (InterruptedException e) {
            System.out.println("Megszakítva");
            e.printStackTrace();
        }
        InputStream i = proc.getInputStream();
        StringBuilder sb = new StringBuilder();
        try {
            for(  int c = 0 ; ( c =  i.read() ) > -1  ; ) {
                sb.append( ( char ) c );
            }
            i.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastFace = sb.toString();  //elmentem egy tagváltozóba
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}