package com.swiftstore.controller;

import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.service.AppUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private AppUserDetailsService appUserDetailsService;

    public AppUserController(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppUserDetails>> all(){
        return ResponseEntity.ok().body(appUserDetailsService.loadAllUsers());
    }

    @GetMapping("/id")
    public ResponseEntity<AppUserDetails> user(@RequestParam("id") Long id){
        return ResponseEntity.ok().body(appUserDetailsService.getById(id)
                .orElseThrow(()->new UsernameNotFoundException("")));
    }
}
