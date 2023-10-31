package com.example.AOManager.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderSupplierRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSupplierDetail {
        private long price;
        private int quantity;
        private String productId;
    }

    private long deliveryDate;
    private long orderDate;
    private String supplierName;
    private String employeeId;
    private List<OrderSupplierDetail> orderSupplierDetailList;
}
