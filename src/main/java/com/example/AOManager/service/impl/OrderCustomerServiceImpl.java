package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.dto.customer.OneOfOrderListDisplayDto;
import com.example.AOManager.dto.manager.OrderCustomerDisplayDto;
import com.example.AOManager.dto.manager.RoleDto;
import com.example.AOManager.entity.CartDetailEntity;
import com.example.AOManager.entity.OrderCustomerEntity;
import com.example.AOManager.entity.OrderStatusEntity;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.payload.request.DoOrderRequest;
import com.example.AOManager.repository.*;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.OrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class OrderCustomerServiceImpl implements OrderCustomerService {

    @Autowired
    OrderCustomerRepository orderCustomerRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Override
    public ApiResponse<?> getOrderCustomer(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            OrderCustomerEntity orderCustomerEntity = this.orderCustomerRepository.findById(UUID.fromString(id)).get();
            OrderCustomerDisplayDto orderCustomerDisplayDto = new OrderCustomerDisplayDto();
            orderCustomerDisplayDto.setId(orderCustomerEntity.getId().toString());
            orderCustomerDisplayDto.setOrderDate(orderCustomerEntity.getOrderDate());
            orderCustomerDisplayDto.setCustomerName(orderCustomerEntity.getCustomerId().getLastName() + " " + orderCustomerEntity.getCustomerId().getFirstName());
            orderCustomerDisplayDto.setDeliveryAddress(orderCustomerEntity.getDeliveryAddress());
            orderCustomerDisplayDto.setDeliveryEmail(orderCustomerEntity.getDeliveryEmail());
            orderCustomerDisplayDto.setDeliveryPhone(orderCustomerEntity.getDeliveryPhone());
            List<OrderCustomerDisplayDto.Product> productList = new ArrayList<>();
            for (CartDetailEntity cartDetail : orderCustomerEntity.getCartDetailList()) {
                OrderCustomerDisplayDto.Product product = new OrderCustomerDisplayDto.Product();
                product.setProductId(cartDetail.getProductId().getId().toString());
                if (cartDetail.getProductId().getProductImageList().size() > 0) {
                    product.setProductImage(cartDetail.getProductId().getProductImageList().get(0).getUrl());
                } else {
                    product.setProductImage(null);
                }
                product.setName(cartDetail.getProductId().getName());
                product.setQuantity(cartDetail.getQuantity());
                product.setInventoryQuantity(cartDetail.getProductId().getInventoryQuantity());
                product.setUnitPrice(cartDetail.getPrice());
                product.setTotalPrice(cartDetail.getQuantity() * cartDetail.getPrice());
                productList.add(product);
            }
            orderCustomerDisplayDto.setProductsList(productList);
            orderCustomerDisplayDto.setTotalPriceOrder(productList.stream().mapToLong(OrderCustomerDisplayDto.Product::getTotalPrice).sum());
            orderCustomerDisplayDto.setOrderStatusId(orderCustomerEntity.getOrderStatusId().getId().toString());
            orderCustomerDisplayDto.setOrderStatusName(orderCustomerEntity.getOrderStatusId().getName());
            orderCustomerDisplayDto.setCreateAt(null != orderCustomerEntity.getCreatedAt() ? Function.toLongFromTimeStamp(orderCustomerEntity.getCreatedAt()) : 0);
            orderCustomerDisplayDto.setUpdateAt(null != orderCustomerEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(orderCustomerEntity.getUpdatedAt()) : 0);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_ORDER_CUSTOMER_SUCCESS, orderCustomerDisplayDto);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_ORDER_CUSTOMER_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getOrderCustomerList(String orderStatusId, int page, int limit, String keyWord) {
        if (CheckInput.stringIsNullOrEmpty(orderStatusId) || !CheckInput.isValidUUID(orderStatusId) || 0 >= page || 0 >= limit) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            long totalResult = this.orderCustomerRepository.getCountRecord(UUID.fromString(orderStatusId), keyWord).get().size();
            int totalPage = (int) Math.ceil((float) totalResult / limit);
            if (page > totalPage && totalPage != 0) {
                page = 1;
            }
            List<OrderCustomerEntity> orderCustomerEntityList = this.orderCustomerRepository.getOrderCustomerList(UUID.fromString(orderStatusId), (page - 1) * limit, limit, keyWord).get();
            List<OrderCustomerDisplayDto> orderCustomerDisplayDtoList = new ArrayList<>();
            for (OrderCustomerEntity orderCustomerEntity : orderCustomerEntityList) {
                OrderCustomerDisplayDto orderCustomerDisplayDto = new OrderCustomerDisplayDto();
                orderCustomerDisplayDto.setId(orderCustomerEntity.getId().toString());
                orderCustomerDisplayDto.setCustomerName(orderCustomerEntity.getCustomerId().getLastName() + " " + orderCustomerEntity.getCustomerId().getFirstName());
                orderCustomerDisplayDto.setDeliveryEmail(orderCustomerEntity.getDeliveryEmail());
                orderCustomerDisplayDto.setDeliveryAddress(orderCustomerEntity.getDeliveryAddress());
                orderCustomerDisplayDto.setDeliveryPhone(orderCustomerEntity.getDeliveryPhone());
                orderCustomerDisplayDto.setOrderDate(orderCustomerEntity.getOrderDate());
                orderCustomerDisplayDto.setTotalPriceOrder(0);
                orderCustomerDisplayDto.setOrderStatusId(orderCustomerEntity.getOrderStatusId().getId().toString());
                orderCustomerDisplayDto.setOrderStatusName(orderCustomerEntity.getOrderStatusId().getName());
                orderCustomerDisplayDto.setProductsList(null);
                orderCustomerDisplayDto.setCreateAt(null != orderCustomerEntity.getCreatedAt() ? Function.toLongFromTimeStamp(orderCustomerEntity.getCreatedAt()) : 0);
                orderCustomerDisplayDto.setUpdateAt(null != orderCustomerEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(orderCustomerEntity.getUpdatedAt()) : 0);
                orderCustomerDisplayDtoList.add(orderCustomerDisplayDto);
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_ORDER_CUSTOMER_LIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, orderCustomerDisplayDtoList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_ORDER_CUSTOMER_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> updateStatusForOrderCustomer(String orderStatusIdTo, String id) {
        if (CheckInput.stringIsNullOrEmpty(orderStatusIdTo) || CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(orderStatusIdTo) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        OrderStatusEntity orderStatusEntity;
        try {
            orderStatusEntity = this.orderStatusRepository.findById(UUID.fromString(orderStatusIdTo)).get();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
        }
        OrderCustomerEntity orderCustomerEntity;
        try {
            orderCustomerEntity = this.orderCustomerRepository.findById(UUID.fromString(id)).get();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
        }
        String currentStatus = orderCustomerEntity.getOrderStatusId().getName();
        switch (orderStatusEntity.getName()) {
            case "Waiting for pick up": {
                if (!currentStatus.equals("Waiting for confirm")) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_UPDATE_ORDER_CUSTOMER_TO_WAITING_PICKUP_FAIL, null);
                } else {
                    orderCustomerEntity.setOrderStatusId(orderStatusEntity);
                }
                break;
            }
            case "Delivering": {
                if (!currentStatus.equals("Waiting for pick up")) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NOT_UPDATE_ORDER_CUSTOMER_TO_DELIVERING_FAIL, null);
                } else {
                    orderCustomerEntity.setOrderStatusId(orderStatusEntity);
                }
                break;
            }
            case "Delivered": {
                if (!currentStatus.equals("Delivering")) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NOT_UPDATE_ORDER_CUSTOMER_TO_DELIVERED_FAIL, null);
                } else {
                    orderCustomerEntity.setOrderStatusId(orderStatusEntity);
                }
                break;
            }
            case "Cancelled": {
                if (!currentStatus.equals("Waiting for confirm")) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NOT_UPDATE_ORDER_CUSTOMER_TO_CANCELLED_FAIL, null);
                } else {
                    orderCustomerEntity.setOrderStatusId(orderStatusEntity);
                }
                break;
            }
            default: {
                return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_STATUS_ORDER_CUSTOMER_FAIL, null);
            }
        }
        try {
            this.orderCustomerRepository.save(orderCustomerEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_UPDATE_STATUS_ORDER_CUSTOMER_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_STATUS_ORDER_CUSTOMER_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getOrdersListForCustomerByStatusId(String orderStatusId, int limit) {
        List<OneOfOrderListDisplayDto> ordersDisplayList;
        try {
            List<OrderCustomerEntity> orderCustomerEntityList = this.orderCustomerRepository.getOrdersListForCustomerByStatusId(UUID.fromString(orderStatusId)).get();
            long total = orderCustomerEntityList.size();
            orderCustomerEntityList = orderCustomerEntityList.subList(0, Math.min(limit, orderCustomerEntityList.size()));
            ordersDisplayList = orderCustomerEntityList.stream().map(OneOfOrderListDisplayDto::new).collect(Collectors.toList());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_ORDER_CUSTOMER_LIST_SUCCESS, new ApiResponseForList<>(total, null, null, null, ordersDisplayList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_ORDER_CUSTOMER_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> doOrder(DoOrderRequest request) {
        if (!CheckInput.isValidName(request.getDeliveryName())
        || !CheckInput.isValidEmail(request.getDeliveryEmail())
        || !CheckInput.isValidPhoneNumber(request.getDeliveryPhone())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            OrderCustomerEntity orderCustomerEntity = new OrderCustomerEntity();
            orderCustomerEntity.setDeliveryAddress(request.getDeliveryAddress());
            orderCustomerEntity.setDeliveryDate(request.getDeliveryDate());
            orderCustomerEntity.setDeliveryEmail(request.getDeliveryEmail());
            orderCustomerEntity.setDeliveryName(Function.normalizeName(request.getDeliveryName()));
            orderCustomerEntity.setDeliveryPhone(request.getDeliveryPhone());
            orderCustomerEntity.setOrderDate(request.getOrderDate());
            orderCustomerEntity.setTotalPrice(request.getTotalPrice());
            orderCustomerEntity.setApproveEmployeeId(null);
            orderCustomerEntity.setDeliveryEmployeeId(null);
            orderCustomerEntity.setCancelEmployeeId(null);
            orderCustomerEntity.setCustomerId(this.usersRepository.findById(UUID.fromString(request.getCustomerId())).get());
            orderCustomerEntity.setOrderStatusId(this.orderStatusRepository.findByName("Waiting for confirm").get());
            OrderCustomerEntity orderCustomerSaved = this.orderCustomerRepository.save(orderCustomerEntity);
            List<CartDetailEntity> cartDetailEntityList = this.cartDetailRepository.getCartDetailsListToOrder(UUID.fromString(request.getCustomerId())).get();
            for (CartDetailEntity cartDetailEntity : cartDetailEntityList) {
                cartDetailEntity.setOrderCustomerId(orderCustomerSaved);
                this.cartDetailRepository.save(cartDetailEntity);
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_DO_ORDER_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DO_ORDER_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> cancelOrder(String orderCustomerId) {
        try {
            OrderStatusEntity orderStatusEntity = this.orderStatusRepository.findByName("Cancelled").get();
            OrderCustomerEntity orderCustomerEntity = this.orderCustomerRepository.findById(UUID.fromString(orderCustomerId)).get();
            orderCustomerEntity.setOrderStatusId(orderStatusEntity);
            this.orderCustomerRepository.save(orderCustomerEntity);
            List<CartDetailEntity> cartDetailEntityList = orderCustomerEntity.getCartDetailList();
            for (CartDetailEntity cartDetailEntity : cartDetailEntityList) {
                ProductEntity productEntity = cartDetailEntity.getProductId();
                productEntity.setInventoryQuantity(productEntity.getInventoryQuantity() + cartDetailEntity.getQuantity());
                this.productRepository.save(productEntity);
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_SUCCESS_PROCESSING, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ERROR_PROCESSING, null);
        }
    }
}
