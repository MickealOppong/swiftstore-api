package com.swiftstore.util;

import org.springframework.stereotype.Component;

@Component
public class ImageStorageLocation {

    private String location="images";

   public String getLocation(){
       return this.location;
   }

}
