package com.swiftstore.service;

import com.swiftstore.controller.AppImageController;
import com.swiftstore.impl.AppImageStorageImpl;
import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.model.photo.AppUserImage;
import com.swiftstore.model.photo.AppImageDetails;
import com.swiftstore.repository.AppImageDetailsRepository;
import com.swiftstore.repository.AppUserDetailsRepository;
import com.swiftstore.repository.AppUserImageRepository;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private AppUserDetailsRepository appUserRepository;
    private AppUserImageRepository appUserImageRepository;
    private AppImageDetailsRepository appImageDetailsRepository;
    private AppImageStorageImpl appImageStorage;

    public AppUserDetailsService(AppUserDetailsRepository appUserRepository,
                                 AppUserImageRepository appUserImageRepository,
                                 AppImageDetailsRepository appImageDetailsRepository,
                                 AppImageStorageImpl appImageStorage) {
        this.appUserRepository = appUserRepository;
        this.appUserImageRepository = appUserImageRepository;
        this.appImageDetailsRepository = appImageDetailsRepository;
        this.appImageStorage = appImageStorage;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username+" does not exist"));
    }

    public List<AppUserDetails> loadAllUsers(){
        return appUserRepository.findAll();
    }

    public Optional<AppUserDetails> getById(Long id){
        Optional<AppUserDetails> userInfo= appUserRepository.findById(id);
        if(userInfo.isPresent()){
            Resource uri = appImageStorage.loadAsResource(imagePath(id));
            String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class,"serveFile",
                            uri.getFilename().toString())
                    .build().toUri().toString();
            userInfo.get().setImageUrl(url);
        }
        return userInfo;
    }

    public Optional<AppUserDetails> getUserById(Long id) {
        return appUserRepository.findById(id);
    }
    private String imagePath(Long id){
        Optional<AppUserImage> userImage = appUserImageRepository.findByUserid(id);
        if(userImage.isPresent()) {
            AppImageDetails imageDetails = appImageDetailsRepository.findById(userImage.get().getImageId()).get();
            return imageDetails.getPath();
        }
        return null;
    }

    public AppUserDetails save(AppUserDetails appUserDetails){
        return appUserRepository.save(appUserDetails);
    }


}
