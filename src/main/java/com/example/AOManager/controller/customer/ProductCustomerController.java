package com.example.AOManager.controller.customer;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer/products")
public class ProductCustomerController {

    @Autowired
    ProductService productService;

    @GetMapping
    ApiResponse<?> getProductsListForCustomer(@RequestParam String categoryId, @RequestParam String orderByPrice, @RequestParam int limit, @RequestParam String keyWord) {return this.productService.getProductsListForCustomer(categoryId, orderByPrice, limit, keyWord);}

    @GetMapping("/{productId}")
    ApiResponse<?> getProductDetail(@PathVariable String productId) {return this.productService.getProduct(productId);}
}
