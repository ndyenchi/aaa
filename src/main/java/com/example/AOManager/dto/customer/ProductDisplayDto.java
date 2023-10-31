package com.example.AOManager.dto.customer;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDisplayDto {

    private String id;
    private String avatar;
    private String name;
    private Long price;
    private Integer soldQuantity;

    public ProductDisplayDto(ProductEntity productEntity) {
        this.setId(productEntity.getId().toString());
        this.setAvatar(productEntity.getImageListString().size() > 0 ? productEntity.getImageListString().get(0) : null);
        this.setName(productEntity.getName());
        this.setPrice(productEntity.getCurrentPrice());
        this.setSoldQuantity(productEntity.getSoldQuantity());
    }
}
