package faceAnalysis;

import logic.Controller;
import logic.RequestHandler;
import view.UI;
import wrappers.CustomDetectedFace;

import java.io.File;
import java.net.URISyntaxException;

public class Main {


	public static void main(String[] args) {

		if (args.length == 0 || args.length>2 ) {
            UI ui = new UI();
            ui.go();
        }
        Controller c = new Controller(new RequestHandler());
        if (args.length == 1) {
            System.out.println(new CustomDetectedFace(c.AnalyseLocalPicture(new File(args[0])).get(0)).toString());
        }
        else if (args.length == 2) {
            c.CompareTwoPictures(new File(args[0]), new File(args[1]));
        }
        else
            System.out.println("Valami probléma volt, írj a főnöknek.");
    }

}
