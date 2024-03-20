package com.swiftstore.service;

import com.swiftstore.impl.AppImageStorageImpl;
import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.model.photo.AppUserImage;
import com.swiftstore.model.photo.AppImageDetails;
import com.swiftstore.repository.AppImageDetailsRepository;
import com.swiftstore.repository.AppUserImageRepository;
import com.swiftstore.util.ImageStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class AppUserImageService {

    private AppImageStorageImpl appImageStorage;
    private AppUserDetailsService appUserDetailsService;
    private AppUserImageRepository appUserImageRepository;
    private AppImageDetailsRepository appImageDetailsRepository;
    private ImageStorageLocation storageLocation;

    public AppUserImageService(AppImageStorageImpl appImageStorage, AppUserDetailsService appUserDetailsService,
                               AppUserImageRepository appUserImageRepository,
                               AppImageDetailsRepository appImageDetailsRepository,
                               ImageStorageLocation storageLocation) {
        this.appImageStorage = appImageStorage;
        this.appUserDetailsService = appUserDetailsService;
        this.appUserImageRepository = appUserImageRepository;
        this.appImageDetailsRepository = appImageDetailsRepository;
        this.storageLocation = storageLocation;
    }

    public void save(MultipartFile file, Long userId){

        AppImageDetails appImageDetails = AppImageDetails.builder()
                .filename(file.getOriginalFilename())
                .fileType(file.getContentType())
                .path(storageLocation.getLocation()+"/"+file.getOriginalFilename())
                .build();

        Optional<AppUserDetails> appUserDetails = appUserDetailsService.getUserById(userId);

        if(appUserDetails.isPresent()){
            AppUserDetails user = appUserDetails.get();
            appImageStorage.store(file);
            appImageDetailsRepository.save(appImageDetails);
            appUserImageRepository.save(new AppUserImage(user.getId(),appImageDetails.getId()));
        }
    }

    public Resource loadResource(String filename){
        return appImageStorage.loadAsResource(filename);
    }
}
