package com.example.AOManager.service;

import com.example.AOManager.dto.manager.ProductDto;
import com.example.AOManager.response.ApiResponse;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProductsList(String categoryId, int page, int limit, String keyWord);

    ApiResponse<?> getProduct(String id);

    ApiResponse<?> createProduct(ProductDto productDto);

    ApiResponse<?> updateProduct(ProductDto productDto);

    ApiResponse<?> deleteProduct(String id);

    long getTotalRecord(String categoryId, String keyWord);

    ApiResponse<?> getProductsListForCustomer(String categoryId, String orderByPrice, int limit, String keyWord);
}
