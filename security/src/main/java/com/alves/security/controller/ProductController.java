package com.alves.security.controller;

import com.alves.security.model.product.Product;
import com.alves.security.model.product.ProductRequest;
import com.alves.security.model.product.ProductResponse;
import com.alves.security.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity postProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = new Product(productRequest);
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getAllProducts() {
        List<ProductResponse> productResponses = productRepository.findAll()
                .stream().map(ProductResponse::new)
                .toList();
        return ResponseEntity.ok().body(productResponses);
    }
}
