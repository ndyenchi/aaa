package com.example.AOManager.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerDisplayDto {

    @Data
    public static class Product {
        private String productId;
        private String productImage;
        private String name;
        private int quantity;
        private int inventoryQuantity;
        private long unitPrice;
        private long totalPrice;
    }

    private String id;
    private long orderDate;
    private String customerName;
    private String deliveryAddress;
    private String deliveryEmail;
    private String deliveryPhone;
    private long totalPriceOrder;
    private String orderStatusId;
    private String orderStatusName;
    private List<Product> productsList;
    private long createAt;
    private long updateAt;
}
