package com.example.AOManager.service;

import com.example.AOManager.request.CreatePriceDetailRequest;
import com.example.AOManager.response.ApiResponse;

public interface PriceDetailService {
    ApiResponse<?> getPriceDetail(String id);

    ApiResponse<?> getPriceDetailsList(String productId);

    ApiResponse<?> createPriceDetail(CreatePriceDetailRequest createPriceDetailRequest);

    ApiResponse<?> deletePriceDetail(String id);
}
