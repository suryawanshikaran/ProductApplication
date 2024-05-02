package com.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.entities.ProductEntity;
import com.task.repo.ProductRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
   
 
 // 1 Create a new product
    public ProductEntity createProduct(ProductEntity product) 
    {
    	System.out.println(product);
        return productRepository.save(product);
    }

    // Check if a product exists by ID
    public boolean isProductExists(Long id) {
        return productRepository.existsById(id);
    }

    // Check if a product exists by name
    public boolean isProductExists(String name) {
        return productRepository.existsByName(name);
    }
    
    
    // Validate the product entity
    private boolean isValidProduct(ProductEntity product) 
    {
        return product != null && product.getName() != null && !product.getName().isEmpty() &&
                product.getDescription() != null && !product.getDescription().isEmpty() &&
                product.getPrice() >= 0;
    }

   
 // 2 getProductById
    @Transactional
    public ProductEntity getProductById(Long productId) 
    {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    // 3 getAllProducts
    @Transactional
    public List<ProductEntity> getAllProducts() 
    {
        return productRepository.findAll();
    }
    
    // 4 updateProduct
    @Transactional
    public ProductEntity updateProduct(Long productId, ProductEntity product) 
    {
        Optional<ProductEntity> existingProductOptional = productRepository.findById(productId);
        if (existingProductOptional.isPresent())
        {
            ProductEntity existingProduct = existingProductOptional.get();
            // Update only if the product is valid
            
            if (isValidProduct(product)) 
            {
                product.setId(productId);
                return productRepository.save(product);
            } 
            else 
            {
                throw new IllegalArgumentException("Invalid product data");
            }
        } 
        
	        else 
	        {
	            throw new IllegalArgumentException("Product not found");
	        }
    }
    	
    // 5 deleteProduct
	    	@Transactional
	    public void deleteProduct(Long productId) 
	    {
	        productRepository.deleteById(productId);
	    }
	
	    
		}
