package com.example.AOManager.dto.manager;

import com.example.AOManager.entity.OrderStatusEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {

    private String id;
    private String name;

    public OrderStatusDto(OrderStatusEntity orderStatusEntity) {
        this.id = orderStatusEntity.getId().toString();
        this.name = orderStatusEntity.getName();
    }
}
