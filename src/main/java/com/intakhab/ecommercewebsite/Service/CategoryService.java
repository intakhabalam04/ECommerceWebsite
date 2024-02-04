package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    boolean addNewCategory(Category category);

    List<Category> getAllCategories();

    boolean deleteCategory(UUID id);
}
