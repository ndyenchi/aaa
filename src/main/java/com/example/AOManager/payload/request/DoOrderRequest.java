package com.example.AOManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoOrderRequest {

    private String customerId;
    private String deliveryAddress;
    private Long orderDate;
    private Long deliveryDate;
    private String deliveryName;
    private String deliveryEmail;
    private String deliveryPhone;
    private Long totalPrice;
}
