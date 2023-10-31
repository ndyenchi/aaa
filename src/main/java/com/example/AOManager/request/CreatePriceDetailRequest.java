package com.example.AOManager.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePriceDetailRequest {
    private long price;
    private long applyDate;
    private String note;
    private String employeeId;
    private String productId;
}
