package com.swiftstore.controller;

import com.swiftstore.service.AppUserImageService;
import com.swiftstore.service.ProductImageService;
import com.swiftstore.util.ImageStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photo")
public class AppImageController {

  private AppUserImageService appUserImageService;
  private ImageStorageLocation imageStorageLocation;
  private ProductImageService productImageService;

    public AppImageController(AppUserImageService appUserImageService,ImageStorageLocation imageStorageLocation) {
        this.appUserImageService = appUserImageService;
        this.imageStorageLocation = imageStorageLocation;
    }

    @PostMapping("/user")
    public void userImage(@RequestParam("photo") MultipartFile file,@RequestParam("userid") Long id){
        appUserImageService.save(file,id);
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = appUserImageService.loadResource(imageStorageLocation.getLocation()+"/"+filename);
        if(file == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\""+ file.getFilename()+"\"").body(file);
    }

    @PostMapping("/product")
    public void productImage(@RequestParam("photo") MultipartFile file,@RequestParam("userid") Long id){
        productImageService.save(file,id);
    }

}
