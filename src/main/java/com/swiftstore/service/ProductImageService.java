package com.swiftstore.service;

import com.swiftstore.impl.AppImageStorageImpl;
import com.swiftstore.model.photo.AppImageDetails;
import com.swiftstore.model.photo.AppProductImage;
import com.swiftstore.model.product.Product;
import com.swiftstore.repository.AppImageDetailsRepository;
import com.swiftstore.repository.AppProductImageRepository;
import com.swiftstore.repository.ProductRepository;
import com.swiftstore.util.ImageStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ProductImageService {

    private ProductRepository productRepository;
    private AppProductImageRepository appProductImageRepository;
    private AppImageStorageImpl appImageStorage;
    private ImageStorageLocation imageStorageLocation;
    private ProductService productService;
    private AppImageDetailsRepository appImageDetailsRepository;

    public ProductImageService(ProductRepository productRepository, AppProductImageRepository appProductImageRepository,
                               AppImageStorageImpl appImageStorage, ImageStorageLocation imageStorageLocation,
                               ProductService productService, AppImageDetailsRepository appImageDetailsRepository) {
        this.productRepository = productRepository;
        this.appProductImageRepository = appProductImageRepository;
        this.appImageStorage = appImageStorage;
        this.imageStorageLocation = imageStorageLocation;
        this.productService = productService;
        this.appImageDetailsRepository = appImageDetailsRepository;
    }

    public void save(MultipartFile file, Long userId){

        AppImageDetails appImageDetails = AppImageDetails.builder()
                .filename(file.getOriginalFilename())
                .fileType(file.getContentType())
                .path(imageStorageLocation.getLocation()+"/"+file.getOriginalFilename())
                .build();

        Optional<Product> product = productService.getProductById(userId);

        if(product.isPresent()){
           Product product1 = product.get();
            appImageStorage.store(file);
            appImageDetailsRepository.save(appImageDetails);
            appProductImageRepository.save(new AppProductImage(product1.getId(),appImageDetails.getId()));
        }
    }
    public Resource loadResource(String filename){
        return appImageStorage.loadAsResource(filename);
    }
}
