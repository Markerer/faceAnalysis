package com.faceanalysis.faceAnalysis.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public List<Admin> getAllAdmins(){
        List<Admin> admins = new ArrayList<>();
        adminRepository.findAll().forEach(admin -> admins.add(admin));
        return admins;
    }

    public Admin getAdminById(int id){
        return adminRepository.findById(id).get();
    }

    public Admin getAdminByFilename(String filename){
        List<Admin> admins = getAllAdmins();
        Admin ret = null;
        for(Admin a : admins){
            if(a.filename.equals(filename)){
                ret = a;
            }
        }
        return ret;
    }

    public void saveOrUpdate(Admin admin){
        adminRepository.save(admin);
    }

    public void delete(int id){
        adminRepository.deleteById(id);
    }

    public void deleteAll(){
        adminRepository.deleteAll();
    }

    public void deleteByFilename(String filename){
        Admin toDelete = getAdminByFilename(filename);
        if(toDelete != null) {
            adminRepository.delete(toDelete);
        }
    }
}
