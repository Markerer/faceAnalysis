package com.faceanalysis.faceAnalysis;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    void changeRootLocation(String location);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    String deleteOne(String filename);

}
