package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Repository.CategoryRepo;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean addNewCategory(Category category) {
        if (category.getCategoryId() != null) {
            Category updateCategory = categoryRepo.findById(category.getCategoryId()).get();
            updateCategory.setCategoryName(category.getCategoryName());
            updateCategory.setCategoryDescription(category.getCategoryDescription());
            categoryRepo.save(updateCategory);
            return true;
        }
        if (categoryRepo.findByCategoryName(category.getCategoryName()) == null) {
            categoryRepo.save(category);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepo.deleteById(id);
    }
}
