package com.faceanalysis.faceAnalysis.database;

import javax.persistence.*;

@Entity
public class Admin {

    @Id
    @GeneratedValue
    public int id;

    @Column(unique = true)
    public String filename;

    @Column(unique = true)
    public String faceId;

    public Admin(String filename, String faceId){
        this.filename = filename;
        this.faceId = faceId;
    }

    public Admin(){}

}
