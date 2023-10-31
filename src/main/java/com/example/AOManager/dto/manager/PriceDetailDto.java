package com.example.AOManager.dto.manager;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.PriceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDetailDto {

    private String id;
    private long price;
    private long applyDate;
    private String note;
    private String employeeId;
    private String employeeName;
    private String productId;
    private long createAt;
    private long updateAt;

    public PriceDetailDto(PriceDetailEntity priceDetailEntity) {
        this.setId(priceDetailEntity.getId().toString());
        this.setPrice(priceDetailEntity.getPrice());
        this.setApplyDate(priceDetailEntity.getApplyDate());
        this.setNote(priceDetailEntity.getNote());
        this.setEmployeeId(priceDetailEntity.getEmployeeId().getId().toString());
        this.setEmployeeName(priceDetailEntity.getEmployeeId().getLastName() + " " + priceDetailEntity.getEmployeeId().getFirstName());
        this.setProductId(priceDetailEntity.getProductId().getId().toString());
        this.setCreateAt(null != priceDetailEntity.getCreatedAt() ? Function.toLongFromTimeStamp(priceDetailEntity.getCreatedAt()) : 0);
        this.setUpdateAt(null != priceDetailEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(priceDetailEntity.getUpdatedAt()) : 0);
    }
}
