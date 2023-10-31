package com.example.AOManager.controller.customer;

import com.example.AOManager.payload.request.AddToCartRequest;
import com.example.AOManager.payload.request.ChangeQuantityRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired
    CartDetailService cartDetailService;

    @GetMapping
    ApiResponse<?> getCart(@RequestParam String customerId) {return this.cartDetailService.getCart(customerId);}

    @PostMapping
    ApiResponse<?> addToCart(@RequestBody AddToCartRequest addToCartRequest) {return this.cartDetailService.addToCart(addToCartRequest);}

    @PutMapping
    ApiResponse<?> changeQuantity(@RequestBody ChangeQuantityRequest changeQuantityRequest) {return this.cartDetailService.changeQuantity(changeQuantityRequest);}

    @DeleteMapping("/{cartDetailId}")
    ApiResponse<?> deleteCartDetail(@PathVariable String cartDetailId) {return this.cartDetailService.deleteCartDetail(cartDetailId);}
}
