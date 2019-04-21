package com.faceanalysis.faceAnalysis.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";
    private String adminLocation = "admin-upload-dir";

    public String getLocation() {
        return location;
    }

    public String getAdminLocation(){
        return adminLocation;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setAdminLocation(String location) {
        this.adminLocation = location;
    }
}