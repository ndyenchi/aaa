package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.dto.customer.ProductOfCartDisplayDto;
import com.example.AOManager.entity.CartDetailEntity;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.entity.UsersEntity;
import com.example.AOManager.payload.request.AddToCartRequest;
import com.example.AOManager.payload.request.ChangeQuantityRequest;
import com.example.AOManager.repository.CartDetailRepository;
import com.example.AOManager.repository.ProductRepository;
import com.example.AOManager.repository.UsersRepository;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public ApiResponse<?> getCart(String customerId) {
        List<ProductOfCartDisplayDto> productsList = new ArrayList<>();
        try {
            List<CartDetailEntity> cartDetailEntityList  = this.cartDetailRepository.findByCustomerId_Id(UUID.fromString(customerId)).get();
            cartDetailEntityList = cartDetailEntityList.stream()
                    .filter(cartDetail -> cartDetail.getOrderCustomerId() == null)
                    .collect(Collectors.toList());
            for (CartDetailEntity cartDetailEntity : cartDetailEntityList) {
                productsList.add(new ProductOfCartDisplayDto(cartDetailEntity));
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCTS_lIST_SUCCESS, productsList);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCTS_lIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> changeQuantity(ChangeQuantityRequest changeQuantityRequest) {
        try {
            CartDetailEntity cartDetailEntity = this.cartDetailRepository.findById(UUID.fromString(changeQuantityRequest.getCartDetailId())).get();
            cartDetailEntity.setQuantity(changeQuantityRequest.getQuantity());
            this.cartDetailRepository.save(cartDetailEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_SUCCESS_PROCESSING, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ERROR_PROCESSING, null);
        }
    }

    @Override
    public ApiResponse<?> addToCart(AddToCartRequest addToCartRequest) {
        try {
            List<CartDetailEntity> cartDetailEntityList = this.cartDetailRepository.getCartDetailsListToAddToCart(UUID.fromString(addToCartRequest.getCustomerId()), UUID.fromString(addToCartRequest.getProductId())).get();
            if(cartDetailEntityList.size() == 0) {
                UsersEntity customer = this.usersRepository.findById(UUID.fromString(addToCartRequest.getCustomerId())).get();
                ProductEntity product = this.productRepository.findById(UUID.fromString(addToCartRequest.getProductId())).get();
                if (!CheckInput.checkInventoryQuantityForCart(product, addToCartRequest.getQuantity())) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_CAN_NOT_ADD_TO_CART_BECAUSE_QUANTITY, null);
                }
                CartDetailEntity newCartDetailEntity = new CartDetailEntity();
                newCartDetailEntity.setQuantity(addToCartRequest.getQuantity());
                newCartDetailEntity.setPrice(addToCartRequest.getUnitPrice());
                newCartDetailEntity.setCustomerId(customer);
                newCartDetailEntity.setProductId(product);
                this.cartDetailRepository.save(newCartDetailEntity);
            } else {
                CartDetailEntity cartDetailEntity = cartDetailEntityList.get(0);
                int quantity = cartDetailEntity.getQuantity() + addToCartRequest.getQuantity();
                if (!CheckInput.checkInventoryQuantityForCart(cartDetailEntity.getProductId(), quantity)) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_CAN_NOT_ADD_TO_CART_BECAUSE_QUANTITY, null);
                }
                cartDetailEntity.setQuantity(quantity);
                this.cartDetailRepository.save(cartDetailEntity);
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_ADD_TO_CART_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ADD_TO_CART_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> deleteCartDetail(String cartDetailId) {
        try {
            this.cartDetailRepository.deleteById(UUID.fromString(cartDetailId));
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_SUCCESS_PROCESSING, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ERROR_PROCESSING, null);
        }
    }
}
