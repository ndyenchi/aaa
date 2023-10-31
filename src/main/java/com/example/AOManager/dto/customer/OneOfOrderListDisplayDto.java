package com.example.AOManager.dto.customer;

import com.example.AOManager.entity.OrderCustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneOfOrderListDisplayDto {

    private String id;
    private String customerName;
    private Long orderDate;

    public OneOfOrderListDisplayDto(OrderCustomerEntity orderCustomerEntity) {
        this.setId(orderCustomerEntity.getId().toString());
        this.setCustomerName(orderCustomerEntity.getCustomerId().getLastName() + " " + orderCustomerEntity.getCustomerId().getFirstName());
        this.setOrderDate(orderCustomerEntity.getOrderDate());
    }
}
