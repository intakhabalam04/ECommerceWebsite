package com.intakhab.ecommercewebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
    @OneToMany(mappedBy = "category")
    private List<Product> productList;

    @Override
    public String toString() {
        return categoryName;
    }
}
