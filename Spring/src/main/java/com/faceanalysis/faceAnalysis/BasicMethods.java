package com.faceanalysis.faceAnalysis;

import java.io.IOException;
import java.io.InputStream;

public class BasicMethods {


    public static String RunFaceAnalysis(String filename, boolean processedResponse) {
        // Run a java app in a separate system process
        Process proc = null;
        try {
            String cmd = "java -jar faceAnalysis.jar ";
            if (filename.contains("http")) {
                cmd += filename;
            } else {
                cmd += "upload-dir/" + filename;
            }
            if(processedResponse){
                cmd += " processed";
            } else {
                cmd += " original";
            }
                proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RunFaceAnalysisApplicationAndReturnOutput(proc);
    }

    public static String RunAdminFaceAnalysis(String filename, boolean processedResponse) {
        // Run a java app in a separate system process
        Process proc = null;
        try {
            String cmd = "java -jar faceAnalysis.jar ";
            if (filename.contains("http")) {
                cmd += filename;
            } else {
                cmd += "admin-upload-dir/" + filename;
            }
            if(processedResponse){
                cmd += " processed";
            } else {
                cmd += " original";
            }
            proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RunFaceAnalysisApplicationAndReturnOutput(proc);
    }


    public static String RunFaceComparison(String filename1, String filename2, boolean processedResponse) {
        // Run a java app in a separate system process
        Process proc = null;
        try {
            String cmd = "java -jar faceAnalysis.jar ";
            if(filename1.contains("http")){
                cmd += filename1;
            } else {
                cmd += "upload-dir/" + filename1;
            }
            cmd += " ";
            if(filename2.contains("http")){
                cmd += filename2;
            } else {
                cmd += "upload-dir/" + filename2;
            }
            if(processedResponse){
                cmd += " processed";
            } else {
                cmd += " original";
            }

            proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RunFaceAnalysisApplicationAndReturnOutput(proc);
    }


    public static String RunAdminFaceComparison(String id1, String id2, boolean processedResponse) {
        // Run a java app in a separate system process
        Process proc = null;
        try {
            String cmd = "java -jar faceAnalysis.jar ";

            cmd += id1 + " " + id2;

            if(processedResponse){
                cmd += " processed";
            } else {
                cmd += " original";
            }

            proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RunFaceAnalysisApplicationAndReturnOutput(proc);
    }


    private static String RunFaceAnalysisApplicationAndReturnOutput(Process process) {

        try {
            process.waitFor(); //bekell várni a jar file-t
        } catch (InterruptedException e) {
            System.out.println("Megszakítva");
            e.printStackTrace();
        }
        InputStream i = process.getInputStream();
        StringBuilder sb = new StringBuilder();
        try {
            for (int c = 0; (c = i.read()) > -1; ) {
                sb.append((char) c);
            }
            i.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
