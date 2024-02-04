package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Service.ProductService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductRepo productRepo;

    public ProductController(ProductService productService, ProductRepo productRepo) {
        this.productService = productService;
        this.productRepo = productRepo;
    }

    @GetMapping("/product/image/{productId}")
    public ResponseEntity<byte[]> fetchProductImage(@PathVariable("productId")UUID productId){
        byte[] imageData = productService.getProductById(productId).getProductImg();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imageData,headers, HttpStatus.OK);
    }

    @PostMapping("/product/upload-image/{productId}")
    public ResponseEntity<String> uploadProductImage(@PathVariable("productId") UUID productId, @RequestParam("image")MultipartFile file){
        try {
            Product product = productService.getProductById(productId);
            product.setProductImg(file.getBytes());
            productRepo.save(product);
            return new ResponseEntity<>("Success",HttpStatus.OK);
        } catch (IOException e) {

            return new ResponseEntity<>("Something wrong",HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
