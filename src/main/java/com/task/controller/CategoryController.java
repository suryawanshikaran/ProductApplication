package com.task.controller;

import org.springframework.web.bind.annotation.RestController;

import com.task.entities.CategoryEntity;
import com.task.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController 
{

    @Autowired
    private CategoryService categoryService;

    // 1 Create a new category
    @PostMapping("/createCategory")
    public String createCategory(@RequestBody CategoryEntity category) 
    {
    	CategoryEntity dbCategory = categoryService.isCategoryExists(category.getName());
    	
    	if(dbCategory==null) {
    		// If not present, create the category
            CategoryEntity createdCategory = categoryService.createCategory(category);
            // Fetch the created category details
            String categoryDetails = getCategoryDetails(createdCategory);
            // Return the response with the category details and success message
            return "Category is created successfully.\n" + categoryDetails;
    	}else {
    		// If not present, create the category
            CategoryEntity createdCategory = categoryService.updateCategory(dbCategory.getId(), category);
            // Fetch the created category details
            String categoryDetails = getCategoryDetails(createdCategory);
            // Return the response with the category details and success message
            return "Category is created successfully.\n" + categoryDetails;
    	}
        
            
        
    }
 

    // Helper method to get category details
    private String getCategoryDetails(CategoryEntity category) 
    {
        StringBuilder details = new StringBuilder();
        details.append("Category ID: ").append(category.getId()).append("\n");
        details.append("Category Name: ").append(category.getName()).append("\n");
        return details.toString();
    }

    // 2 Retrieve all categories with pagination
    @GetMapping ("/getAllCategories")
    public String getAllCategories(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) 
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> categoriesPage = categoryService.getAllCategories(pageable);
        
        StringBuilder response = new StringBuilder("All categories:\n");
        for (CategoryEntity category : categoriesPage.getContent())
        {
            response.append(category.toString()).append("\n");
        }
        return response.toString();
    }


    // 3 Retrieve a category by ID
    @GetMapping("/getCategoryById/{id}")
    public String getCategoryById(@PathVariable("id") Long id) 
    {
        Optional<CategoryEntity> category = categoryService.getCategoryById(id);
        if (category.isPresent()) 
        {
            return "Category found: " + category.get().toString();
        } 
        else 
        {
            return "Category with ID " + id + " not found";
        }
    }

    // 4 Update a category by ID
    @PutMapping("/updateCategory/{id}")
    public CategoryEntity updateCategory(@PathVariable("id") Long id,
                                     @RequestBody CategoryEntity categoryDetails)
    {
     CategoryEntity updatedCategory =  categoryService.updateCategory(id, categoryDetails);
        return updatedCategory;
    }
    

    // 5 Delete a category by ID
    @DeleteMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) 
    {
    	categoryService.deleteCategory(id);
        return "category is deleted Successfully";
    }

}
