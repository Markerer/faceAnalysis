package com.faceanalysis.faceAnalysis;

import com.faceanalysis.faceAnalysis.database.Admin;
import com.faceanalysis.faceAnalysis.database.AdminService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Controller
public class AnalysisRequestController {

    private final StorageService storageService;

    private final AdminService adminService;

    @Autowired
    public AnalysisRequestController(StorageService storageService, AdminService adminService) {
        this.storageService = storageService;
        this.adminService = adminService;
    }


    // use like http://localhost:8080/analysis?filename=face1.jpg
    @RequestMapping(value = "/analysis", method = GET, produces = "application/json")
    public ResponseEntity<String> getFaceAnalysis(@RequestParam("filename") String filename) {
        File file = new File("upload-dir" +
                "/" + filename);
        if (file.exists() || filename.contains("http")) {
            String json = BasicMethods.RunFaceAnalysis(filename, true);
            // amennyiben lekérik, és nem tartalmaz egy arcot sem, akkor töröljük
            if (!json.contains("{")) {
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

                Admin tryer = null;

                List<Admin> admins = new ArrayList<>();

                // Az elérési utak kiegészítése, hiszen az adatbázisban csak a fájlneveket tudjuk eltárolni
                // az időzített feladat limitációi miatt
                for (Admin a : adminService.getAllAdmins()) {
                    // önmagával ne legyen összehasonlítva
                    if (a.filename.equals(filename)) {
                        tryer = a;
                        continue;
                    }
                    String location = MvcUriComponentsBuilder.fromMethodCall(on(FileUploadController.class)
                            .serveAdminFile(a.filename)).build().toString();
                    System.out.println(location);
                    Admin temp = new Admin(location, a.faceId);
                    admins.add(temp);
                }

                ExecutorService executor = Executors.newFixedThreadPool(20);

                for (Admin admin : admins) {
                    Admin finalTryer = tryer;
                    executor.submit(() -> {
                        // az összehasonlítás lefuttatása a már megkapott faceId-kkel
                        if(!finalTryer.faceId.equals(admin.faceId)) {
                            System.out.println("Comparing id: " + finalTryer.faceId + " TO: " + admin.faceId);
                            String verifyResult = BasicMethods.RunAdminFaceComparison(finalTryer.faceId, admin.faceId, true);
                            System.out.println(verifyResult);
                            if (verifyResult.contains("{")) {
                                responses.add(verifyResult);
                                fileLocations.add(admin.filename);
                            }
                        }
                    });
                }
                // bevárjuk, hogy az összes végezzen
                awaitTerminationAfterShutdown(executor);

                for (int i = 0; i < responses.size(); i++) {
                    String result = responses.get(i);
                    String fileLocation = fileLocations.get(i);

                    JSONObject jsonObject = new JSONObject(result);

                    double temp = (double) jsonObject.get("Confidence");
                    if (temp > confidence) {
                        confidence = temp;
                        pathToMostSimilarImage = fileLocation;
                        mostSimilarJsonObject = jsonObject;
                    }
                }
                try {
                    mostSimilarJsonObject.put("Image", pathToMostSimilarImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // a beadott kép törlése a szerverről és az adatbázisból is
                storageService.deleteOne(filename);
                adminService.delete(tryer.id);
                return ResponseEntity.ok(mostSimilarJsonObject.toString());
        } else {
            return ResponseEntity.badRequest().body("The given file doesn't exist on our server.");
        }
    }


    private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
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


    // 6 óráként az admin faceId-k begyűjtése, kezdeti késleltetés szükséges, mert anélkül valamiért
    // figyelem kívül hagyja a mappaváltást és rossz mappát kezd el átnézni
    // a késleltetések mértékegysége ms
    @Scheduled(fixedRate = 21600000, initialDelay = 1000)
    public void refreshAdminFaceIds() {
        storageService.changeRootLocation("admin-upload-dir");
        // adatbázis ürítése
        adminService.deleteAll();

        List<String> locations = new ArrayList<>();
        Object[] files = storageService.loadAll().toArray();
        // sajnos az időzített feladatból nem lehet HTTP kérést indítani, így nem lehetséges
        // a teljes elérési útvonal eltárolása (amin a serveAdminFile is elérhető), csak a fájlnevet tudjuk
        for (Object o : files) {
            // fájlnevek berakása listába
            locations.add(o.toString());
        }

        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (String file : locations) {
            executor.submit(() -> {
                // eredeti (nem a becsomagolt = feldolgozott) választ várjuk, hiszen abban benne van a faceId is
                String json = BasicMethods.RunAdminFaceAnalysis(file, false);
                System.out.println(json);
                // ellenőrizzük, hogy található-e arc a válaszban, azaz a képen
                // és csak akkor rakjuk az adatbázisba, ha igen.
                if (json.contains("{")) {
                    try {
                        // a válasz viszont minden esetben tömb, ezért kell így feldolgozni
                        JSONArray jsonArray = new JSONArray(json);
                        // a legkönnyebben (azaz az első) felismerhető arcot vesszük figyelembe
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        String faceId = (String) jsonObject.get("faceId");
                        Admin temp = new Admin(file, faceId);
                        // adatbázisba elmentés
                        adminService.saveOrUpdate(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        awaitTerminationAfterShutdown(executor);
        // csak ideiglenesen, logolásnak
        List<Admin> admins = adminService.getAllAdmins();
        for (Admin a : admins) {
            System.out.println(a.filename + '\t' + a.faceId);
        }
        System.out.println(admins.size());
    }
}
