package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Repository.CategoryRepo;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    // Method to add a new category or update an existing one
    @Override
    public boolean addNewCategory(Category category) {
        if (category.getCategoryId() != null) {
            // If the category ID exists, update the existing category
            Category updateCategory = categoryRepo.findById(category.getCategoryId()).get();
            updateCategory.setCategoryName(category.getCategoryName());
            updateCategory.setCategoryDescription(category.getCategoryDescription());
            categoryRepo.save(updateCategory);
            return true; // Category updated successfully
        }

        // If the category name is not already present, save the new category
        if (categoryRepo.findByCategoryName(category.getCategoryName()) == null) {
            categoryRepo.save(category);
            return true; // New category added successfully
        }

        return false; // Category not added or updated
    }

    // Method to get all categories
    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // Method to delete a category by ID
    @Override
    public boolean deleteCategory(UUID id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // Check if the category has associated products
            if (category.getProductList().isEmpty()) {
                categoryRepo.deleteById(id);
                return true; // Category deleted successfully
            } else {
                return false; // Category has associated products, cannot be deleted
            }
        }
        return false; // Category not found
    }
}
