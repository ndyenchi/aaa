package com.example.AOManager.dto.manager;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String name;
    private String description;
    private String habitat;
    private Double temperature;
    private Double ph;
    private String position;
    private String reproductionMethod;
    private String foodType;
    private Double maxSize;
    private Integer inventoryQuantity;
    private Integer soldQuantity;
    private Boolean status;
    private String categoryId;
    private String categoryName;
    private List<String> imageList;
    private Long price;
    private Long createAt;
    private Long updateAt;

    public ProductDto(ProductEntity productEntity) {
        this.id = productEntity.getId().toString();
        this.name = productEntity.getName();
        this.description = productEntity.getDescription();
        this.habitat = productEntity.getHabitat();
        this.temperature = productEntity.getTemperature();
        this.ph = productEntity.getPh();
        this.position = productEntity.getPosition();
        this.reproductionMethod = productEntity.getReproductionMethod();
        this.foodType = productEntity.getFoodType();
        this.maxSize = productEntity.getMaxSize();
        this.inventoryQuantity = productEntity.getInventoryQuantity();
        this.soldQuantity = productEntity.getSoldQuantity();
        this.status = productEntity.getStatus();
        this.categoryId = productEntity.getCategoryId().getId().toString();
        this.categoryName = productEntity.getCategoryId().getName();
        this.imageList = productEntity.getImageListString();
        this.price = productEntity.getCurrentPrice();
        this.createAt = null != productEntity.getCreatedAt() ? Function.toLongFromTimeStamp(productEntity.getCreatedAt()) : 0;
        this.updateAt = null != productEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(productEntity.getUpdatedAt()) : 0;
    }

    public ProductEntity toEntity() {
        ProductEntity productEntity = new ProductEntity();
        if(!CheckInput.stringIsNullOrEmpty(this.getId())) {
            productEntity.setId(UUID.fromString(this.getId()));
        }
        productEntity.setDescription(this.getDescription());
        productEntity.setFoodType(this.getFoodType());
        productEntity.setHabitat(this.getHabitat());
        productEntity.setMaxSize(this.getMaxSize());
        productEntity.setName(Function.normalizeName(this.getName()));
        productEntity.setPh(this.getPh());
        productEntity.setPosition(this.getPosition());
        productEntity.setReproductionMethod(this.getReproductionMethod());
        productEntity.setTemperature(this.getTemperature());
        productEntity.setInventoryQuantity(this.getInventoryQuantity());
        productEntity.setSoldQuantity(this.getSoldQuantity());
        return productEntity;
    }
}
