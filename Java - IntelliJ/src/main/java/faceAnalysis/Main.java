package faceAnalysis;

import logic.Controller;
import logic.RequestHandler;
import view.UI;

import java.io.File;
import java.net.URISyntaxException;

public class Main {


	public static void main(String[] args) {

		if (args.length == 0 || args.length>2 ) {
            UI ui = new UI();
            ui.go();
        }
        try {
            Controller c = new Controller(new RequestHandler());
            if (args.length == 1) {
                c.AnalyseLocalPicture(new File(args[0]));
            }
            if (args.length == 2) {
                c.CompareTwoPictures(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString()+ args[0]),
                        new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + args[1]));
            }
        }
        catch (URISyntaxException e){
		    e.getMessage();
            System.out.println("Probléma a fálj beolvasása közben.");
        }

    }

}
