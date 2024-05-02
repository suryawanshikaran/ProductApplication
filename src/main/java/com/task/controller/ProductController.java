package com.task.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.entities.CategoryEntity;
import com.task.entities.ProductEntity;
import com.task.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
/*
    @PostMapping("/create")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        ProductEntity createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    */
    
    @PostMapping("/createProduct")
    public ResponseEntity<String> createProduct(@RequestBody ProductEntity product) 
    {
       
            // If not present, create the product
            ProductEntity createdProduct = productService.createProduct(product);
            // Fetch the created product details
            String productDetails = getProductDetails(createdProduct);
            // Return the response with the product details and success message
            return ResponseEntity.status(HttpStatus.CREATED).body("Product is created successfully.\n" + productDetails);
        
    }

    // Helper method to get product details
    private String getProductDetails(ProductEntity product) {
        StringBuilder details = new StringBuilder();
        details.append("Product ID: ").append(product.getId()).append("\n");
        details.append("Product Name: ").append(product.getName()).append("\n");
        details.append("Product Description: ").append(product.getDescription()).append("\n");
        details.append("Product Price: ").append(product.getPrice()).append("\n");
        
        return details.toString();
    }
    
	// Retrieve a Product by ID
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable("id") Long productId) {
        ProductEntity product = productService.getProductById(productId);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Retrieve all Products
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    // Update Product ById
    @PutMapping("updateProduct/{Id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long productId, @RequestBody ProductEntity product) {
        try 
        {
            ProductEntity updatedProduct = productService.updateProduct(productId, product);
            return ResponseEntity.ok(updatedProduct);
        } 
        catch (IllegalArgumentException ex) 
        {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Product ById
    @DeleteMapping("/deleteProduct/{Id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
