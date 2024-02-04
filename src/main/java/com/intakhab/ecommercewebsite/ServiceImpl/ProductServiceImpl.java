package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    // Constructor injection to initialize the ProductRepo
    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    // Retrieve all products from the database
    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Add a new product to the database
    @Override
    public boolean addNewProduct(Product product) {
        try {
            // Save the new product to the database
            productRepo.save(product);
            return true; // Product added successfully
        } catch (Exception e) {
            // Handle any exceptions that occur during the product addition process
            System.out.println(e.getMessage());
            return false; // Product addition failed
        }
    }

    // Delete a product from the database based on its ID
    @Override
    public void delete(UUID id) {
        productRepo.deleteById(id);
    }

    // Retrieve a product from the database based on its ID
    @Override
    public Product getProductById(UUID id) {
        return productRepo.findById(id).orElse(null);
    }
}
