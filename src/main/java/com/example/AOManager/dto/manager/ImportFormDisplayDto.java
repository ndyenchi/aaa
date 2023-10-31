package com.example.AOManager.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportFormDisplayDto {

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
    private String orderSupplierId;
    private long createDate;
    private String supplierName;
    private String employeeName;
    private List<Product> productsList;
    private long totalPriceImportForm;
    private long createAt;
    private long updateAt;
}
