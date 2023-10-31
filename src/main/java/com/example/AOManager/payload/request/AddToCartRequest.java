package com.example.AOManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    private Integer quantity;
    private Long unitPrice;
    private String productId;
    private String customerId;
}
