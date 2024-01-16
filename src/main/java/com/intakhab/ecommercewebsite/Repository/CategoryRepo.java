package com.intakhab.ecommercewebsite.Repository;

import com.intakhab.ecommercewebsite.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    Category findByCategoryName(String name);
}
