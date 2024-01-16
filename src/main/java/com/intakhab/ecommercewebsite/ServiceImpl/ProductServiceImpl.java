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

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public boolean addNewProduct(Product product) {

        try {
            productRepo.save(product);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(UUID id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepo.findById(id).get();
    }
}
