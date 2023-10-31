package com.example.AOManager.service.impl;

import com.example.AOManager.common.Function;
import com.example.AOManager.dto.customer.ProductDisplayDto;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.repository.ProductRepository;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public ApiResponse<?> getNewProductsList() {
        List<ProductDisplayDto> newProductDisplayDtoList;
        try {
            List<ProductEntity> newProductsListInit = this.productRepository.getNewProductsList().get();
            List<ProductEntity> newProductsList = Function.removeProductWithNoPrice(newProductsListInit);
            newProductDisplayDtoList = newProductsList.stream().map(ProductDisplayDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCTS_lIST_FAIL, null);
        }
        return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCTS_lIST_SUCCESS, newProductDisplayDtoList);
    }

    @Override
    public ApiResponse<?> getBestSellingProductsList() {
        List<ProductDisplayDto> bestSellingProductDisplayDtoList;
        try {
            List<ProductEntity> bestSellingProductsListInit = this.productRepository.getBestSellingProductsList().get();
            List<ProductEntity> bestSellingProductsList = Function.removeProductWithNoPrice(bestSellingProductsListInit);
            bestSellingProductDisplayDtoList = bestSellingProductsList.stream().map(ProductDisplayDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCTS_lIST_FAIL, null);
        }
        return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCTS_lIST_SUCCESS, bestSellingProductDisplayDtoList);
    }
}
