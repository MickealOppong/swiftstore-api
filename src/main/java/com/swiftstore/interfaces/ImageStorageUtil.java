package com.swiftstore.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ImageStorageUtil {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll(String filename);

    Path toPath(String filename);

    Resource loadAsResource(String filename);

    void delete();

}
