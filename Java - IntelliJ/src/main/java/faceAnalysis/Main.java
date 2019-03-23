package faceAnalysis;

import logic.RequestHandler;
import view.UI;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {


	public static void main(String[] args) {

		if (args.length == 0 || args.length>2 ) {
            UI ui = new UI();
            ui.go();
        }
        RequestHandler rh = new RequestHandler();
        if (args.length == 1) {
            if(args[0].contains("http")){
                System.out.println(rh.buildAndSendHttpRequestFromURL(args[0]));
            } else {
                System.out.println(rh.buildAndSendHttpRequestFromLocalContent(new File(args[0])));
            }
        }
        else if (args.length == 2) {
            String file1Id = "";
            String file2Id = "";

            if(args[0].contains("http")){
                String json = rh.buildAndSendHttpRequestFromURL(args[0]);
                file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
            } else {
                String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[0]));
                file1Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
            }
            if(args[1].contains("http")){
                String json = rh.buildAndSendHttpRequestFromURL(args[1]);
                file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
            } else {
                String json = rh.buildAndSendHttpRequestFromLocalContent(new File(args[1]));
                file2Id = rh.getDetectedFacesFromJson(json).get(0).faceId().toString();
            }
            System.out.println(rh.sendVerifyRequest(file1Id, file2Id));
        }
        else
            System.out.println("Valami probléma volt, írj a főnöknek.");
    }

}
