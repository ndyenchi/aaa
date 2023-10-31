package com.example.AOManager.service;

import com.example.AOManager.response.ApiResponse;

public interface HomeService {
    ApiResponse<?> getNewProductsList();

    ApiResponse<?> getBestSellingProductsList();
}
