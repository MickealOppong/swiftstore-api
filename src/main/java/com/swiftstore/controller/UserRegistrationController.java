package com.swiftstore.controller;

import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.model.User.UserRegistrationRequest;
import com.swiftstore.repository.AppUserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class UserRegistrationController {

    private AppUserDetailsRepository appUserRepo;
    private PasswordEncoder passwordEncoder;

    public UserRegistrationController(AppUserDetailsRepository appUserRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    public String add(@RequestBody UserRegistrationRequest userRegistrationRequest){
        System.out.println(userRegistrationRequest);
        AppUserDetails userDetails =appUserRepo.save(new AppUserDetails(userRegistrationRequest.username(),
                passwordEncoder.encode(userRegistrationRequest.password())));
        if(userDetails != null){
            return "User created";
        }
        return null;
    }

}
