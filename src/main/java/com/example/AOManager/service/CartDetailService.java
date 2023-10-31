package com.example.AOManager.service;

import com.example.AOManager.payload.request.AddToCartRequest;
import com.example.AOManager.payload.request.ChangeQuantityRequest;
import com.example.AOManager.response.ApiResponse;

public interface CartDetailService {
    ApiResponse<?> getCart(String id);

    ApiResponse<?> changeQuantity(ChangeQuantityRequest changeQuantityRequest);

    ApiResponse<?> addToCart(AddToCartRequest addToCartRequest);

    ApiResponse<?> deleteCartDetail(String cartDetailId);
}
