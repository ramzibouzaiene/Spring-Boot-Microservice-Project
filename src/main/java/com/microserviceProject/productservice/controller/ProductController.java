package com.microserviceProject.productservice.controller;

import com.microserviceProject.productservice.dto.ProductRequest;
import com.microserviceProject.productservice.dto.ProductResponse;
import com.microserviceProject.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
         productService.createProduct(productRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllproducts(){
        return productService.getAllProducts();
    }
}
