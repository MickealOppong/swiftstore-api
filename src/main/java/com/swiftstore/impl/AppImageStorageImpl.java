package com.swiftstore.impl;

import com.swiftstore.exceptions.ImageStorageException;
import com.swiftstore.exceptions.ImageStorageNotFoundException;
import com.swiftstore.interfaces.ImageStorageUtil;
import com.swiftstore.util.ImageStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Component
public class AppImageStorageImpl implements ImageStorageUtil {

    private Path root;

    private ImageStorageLocation imageStorageLocation;

    public AppImageStorageImpl(ImageStorageLocation imageStorageLocation){
        this.imageStorageLocation = imageStorageLocation;

        if(imageStorageLocation.getLocation().trim().length()==0){
            throw new ImageStorageException("Could not initialize empty folder");
        }
        root = Paths.get(imageStorageLocation.getLocation());
    }

    @Override
    public void init(){
        try{
            if (!Files.exists(root)){
                Files.createDirectories(root);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void store(MultipartFile file) {
        try{
            if(file.isEmpty()){
                throw new ImageStorageException("Failed to store empty file");
            }

            Path destination = root.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
            if(!destination.getParent().equals(this.root.toAbsolutePath())){
                throw new ImageStorageException("File cannot be stored outside the current directory");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream,destination, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e){
            throw new ImageStorageException("Failed to store file");
        }
    }

    @Override
    public Stream<Path> loadAll(String filename){
        try {
            return Files.walk(root,1).filter(file->!file.equals(root)).map(
                    this.root::relativize);
        } catch (IOException e) {
            throw new ImageStorageException("Failed to read files",e);
        }

    }


    @Override
    public Path toPath(String filename) {
        return Path.of(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = toPath(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new ImageStorageNotFoundException("Count not read file "+filename);
            }
        } catch (MalformedURLException e) {
            throw new ImageStorageNotFoundException("Count not read file"+filename,e);
        }
    }

    @Override
    public void delete() {

    }

    public static void main(String[] args) {
        AppImageStorageImpl ap =new AppImageStorageImpl(new ImageStorageLocation());
        System.out.println(ap.loadAsResource("img-9.jpeg"));
    }
}
