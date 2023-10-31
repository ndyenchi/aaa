package com.example.AOManager.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateImportFormRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportDetail {
        private long price;
        private int quantity;
        private String importId;
        private String productId;
    }

    private long createDate;
    private String employeeId;
    private String orderSupplierId;
    private List<ImportDetail> importDetailList;
}
