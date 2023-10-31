package com.example.AOManager.service.impl;

import com.example.AOManager.dto.manager.OrderStatusDto;
import com.example.AOManager.entity.OrderStatusEntity;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.repository.OrderStatusRepository;
import com.example.AOManager.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Override
    public ApiResponse<?> getAllOrderStatus() {
        try {
            List<OrderStatusEntity> orderStatusList = this.orderStatusRepository.findAll(Sort.by(Sort.Order.asc("numOrder")));
            List<OrderStatusDto> orderStatusDtoList = orderStatusList.stream().map(OrderStatusDto::new).collect(Collectors.toList());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_ORDER_STATUS_LIST_SUCCESS, orderStatusDtoList);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_ORDER_STATUS_LIST_FAIL, null);
        }
    }
}
