package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getAllProducts();
    boolean addNewProduct(Product product);
    void delete(UUID id);
    Product getProductById(UUID id);
}
