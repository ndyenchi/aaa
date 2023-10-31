package com.example.AOManager.service;

import com.example.AOManager.request.CreateOrderSupplierRequest;
import com.example.AOManager.response.ApiResponse;

public interface OrderSupplierService {

    ApiResponse<?> getOrderSupplier(String id);

    ApiResponse<?> getOrderSupplierList(String status, int page, int limit, String keyWord);

    ApiResponse<?> cancelOrderSupplier(String id);

    ApiResponse<?> createOrderSupplier(CreateOrderSupplierRequest createOrderSupplierRequest);
}
