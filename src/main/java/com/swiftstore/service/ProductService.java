package com.swiftstore.service;

import com.swiftstore.controller.AppImageController;
import com.swiftstore.impl.AppImageStorageImpl;
import com.swiftstore.model.photo.AppImageDetails;
import com.swiftstore.model.photo.AppProductImage;
import com.swiftstore.model.product.Product;
import com.swiftstore.repository.*;
import com.swiftstore.util.ImageStorageLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private ProductRepository productRepository;
    private ImageStorageLocation imageStorageLocation;
    private AppProductImageRepository appProductImageRepository;
    private AppImageDetailsRepository appImageDetailsRepository;
    private AppImageStorageImpl appImageStorage;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductColoursRepository productColoursRepository;

    public ProductService(ProductRepository productRepository, ImageStorageLocation imageStorageLocation,
                          AppProductImageRepository appProductImageRepository,
                          AppImageDetailsRepository appImageDetailsRepository,
                          AppImageStorageImpl appImageStorage) {
        this.productRepository = productRepository;
        this.imageStorageLocation = imageStorageLocation;
        this.appProductImageRepository = appProductImageRepository;
        this.appImageDetailsRepository = appImageDetailsRepository;
        this.appImageStorage = appImageStorage;
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> all(){
        List<Product> productList =  productRepository.findAll();
        for(Product product : productList){
            List<String> links = imagePath(product.getId());
            for(String str : links){
               Resource url = appImageStorage.loadAsResource(str);
                String uri = MvcUriComponentsBuilder.fromMethodName(AppImageController.class,"serveFile",
                                url.getFilename().toString())
                        .build().toUri().toString();
               product.getImgs().add(uri);
            }
        }
        return productList;
    }

    private List<String> imagePath(Long id){
        List<String> paths = new ArrayList<>();
        List<AppProductImage> productImage = appProductImageRepository.findByProductId(id);
        for(AppProductImage image : productImage) {
           Optional<AppImageDetails> imageDetails = appImageDetailsRepository.findById(image.getId());
            imageDetails.ifPresent(appImageDetails -> paths.add(appImageDetails.getPath()));
        }
        return paths;
    }

    public Product save(Product product){
        Product savedProduct = productRepository.save(product);
       product.getProductCategory().forEach(p->productCategoryRepository.save(p));
       product.getProductColours().forEach(p->productColoursRepository.save(p));
       return savedProduct;
    }

    @Transactional
    public Product save(Product product, MultipartFile file){
        AppImageDetails appImageDetails = AppImageDetails.builder()
                .filename(file.getOriginalFilename())
                .fileType(file.getContentType())
                .path(imageStorageLocation.getLocation()+"/"+file.getOriginalFilename())
                .build();
       Product savedProduct= productRepository.save(product);
        appImageDetailsRepository.save(appImageDetails);
        appImageStorage.store(file);
       AppProductImage f= appProductImageRepository.save(new AppProductImage(savedProduct.getId(), appImageDetails.getId()));
        product.getProductCategory().forEach(p->productCategoryRepository.save(p));
        product.getProductColours().forEach(p->productColoursRepository.save(p));
        return savedProduct;
    }

    @Transactional
    public Product save(Product product, MultipartFile[] files){

        Product savedProduct= productRepository.save(product);
        Arrays.stream(files).forEach(file -> {
            AppImageDetails appImageDetails = AppImageDetails.builder()
                    .filename(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .path(imageStorageLocation.getLocation()+"/"+file.getOriginalFilename())
                    .build();
            appImageDetailsRepository.save(appImageDetails);
            appProductImageRepository.save(new AppProductImage(savedProduct.getId(), appImageDetails.getId()));
        });
        Arrays.stream(files).forEach(file -> appImageStorage.store(file));
        product.getProductCategory().forEach(p->productCategoryRepository.save(p));
        product.getProductColours().forEach(p->productColoursRepository.save(p));
        return savedProduct;
    }

}
