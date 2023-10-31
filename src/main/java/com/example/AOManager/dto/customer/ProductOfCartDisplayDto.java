package com.example.AOManager.dto.customer;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.entity.CartDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.AOManager.common.Message.MSG_INVENTORY_NOT_ENOUGH;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfCartDisplayDto {

    private String id;
    private String imageUrl;
    private String name;
    private Integer quantity;
    private Long unitPrice;
    private int inventoryQuantity;
    private String errQuantity;

    public ProductOfCartDisplayDto(CartDetailEntity cartDetailEntity) {
        this.setId(cartDetailEntity.getId().toString());
        this.setImageUrl(cartDetailEntity.getProductId().getImageListString().size() > 0 ? cartDetailEntity.getProductId().getImageListString().get(0) : null);
        this.setName(cartDetailEntity.getProductId().getName());
        this.setQuantity(cartDetailEntity.getQuantity());
        this.setUnitPrice(cartDetailEntity.getPrice());
        this.setInventoryQuantity(cartDetailEntity.getProductId().getInventoryQuantity());
        this.setErrQuantity(CheckInput.checkInventoryQuantityForCart(cartDetailEntity.getProductId(), this.getQuantity()) ? null : MSG_INVENTORY_NOT_ENOUGH + "only " +  this.getInventoryQuantity() + " left!");
    }
}
