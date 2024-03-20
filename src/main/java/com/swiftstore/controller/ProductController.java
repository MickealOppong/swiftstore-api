package com.swiftstore.controller;

import com.swiftstore.model.product.Product;
import com.swiftstore.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public Product product(@RequestBody Product product){
        return productService.save(product);
    }

    @PostMapping("/productAndImage")
    public Product product(@RequestBody Product product, @RequestParam("image")MultipartFile file){
        return productService.save(product,file);
    }

    @PostMapping("/productAndImages")
    public Product product(@RequestBody Product product, @RequestParam("images")MultipartFile[] files){
        return productService.save(product,files);
    }

    @GetMapping("/all")
    public List<Product> all(){
        return productService.all();
    }
}
