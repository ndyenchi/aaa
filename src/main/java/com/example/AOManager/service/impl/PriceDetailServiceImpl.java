package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.dto.manager.PriceDetailDto;
import com.example.AOManager.entity.PriceDetailEntity;
import com.example.AOManager.repository.PriceDetailRepository;
import com.example.AOManager.repository.ProductRepository;
import com.example.AOManager.repository.UsersRepository;
import com.example.AOManager.request.CreatePriceDetailRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.PriceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class PriceDetailServiceImpl implements PriceDetailService {

    @Autowired
    PriceDetailRepository priceDetailRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public ApiResponse<?> getPriceDetail(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            PriceDetailDto priceDetailDto = new PriceDetailDto(this.priceDetailRepository.findById(UUID.fromString(id)).get());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRICE_DETAIL_SUCCESS, priceDetailDto);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRICE_DETAIL_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getPriceDetailsList(String productId) {
        if (CheckInput.stringIsNullOrEmpty(productId) || !CheckInput.isValidUUID(productId)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            List<PriceDetailEntity> priceDetailEntityList = this.priceDetailRepository.getPriceDetailsListWithProductId(UUID.fromString(productId)).get();
            List<PriceDetailDto> priceDetailDtoList = priceDetailEntityList.stream().map(PriceDetailDto::new).collect(Collectors.toList());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRICE_DETAIL_LIST_SUCCESS, priceDetailDtoList);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRICE_DETAIL_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> createPriceDetail(CreatePriceDetailRequest createPriceDetailRequest) {
        if (null == createPriceDetailRequest || 0 >= createPriceDetailRequest.getPrice() || !CheckInput.isValidDateForPriceDetail(createPriceDetailRequest.getApplyDate())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST_FOR_PRICE_DETAIL, null);
        }
        List<PriceDetailEntity> priceDetailEntityList = this.priceDetailRepository.getPriceDetailsListWithProductId(UUID.fromString(createPriceDetailRequest.getProductId())).get();
        if (priceDetailEntityList.stream().anyMatch(priceDetail -> priceDetail.getApplyDate() == createPriceDetailRequest.getApplyDate())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_PRICE_DETAIL_EXISTED, null);
        }
        try {
            PriceDetailEntity priceDetailEntity = new PriceDetailEntity();
            priceDetailEntity.setPrice(createPriceDetailRequest.getPrice());
            priceDetailEntity.setApplyDate(createPriceDetailRequest.getApplyDate());
            priceDetailEntity.setNote(CheckInput.stringIsNullOrEmpty(createPriceDetailRequest.getNote()) ? null : createPriceDetailRequest.getNote());
            priceDetailEntity.setEmployeeId(this.usersRepository.findById(UUID.fromString(createPriceDetailRequest.getEmployeeId())).get());
            priceDetailEntity.setProductId(this.productRepository.findById(UUID.fromString(createPriceDetailRequest.getProductId())).get());
            this.priceDetailRepository.save(priceDetailEntity);
            return new ApiResponse<>(HttpStatus.CREATED.value(), MSG_CREATE_PRICE_DETAIL_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CREATE_PRICE_DETAIL_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> deletePriceDetail(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        PriceDetailEntity priceDetailEntity = this.priceDetailRepository.findById(UUID.fromString(id)).get();
        if (priceDetailEntity.getPrice() == priceDetailEntity.getProductId().getCurrentPrice()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_CANT_NOT_DELETE_PRICE_DETAIL, null);
        }
        try {
            this.priceDetailRepository.delete(priceDetailEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_DELETE_PRICE_DETAIL_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRICE_DETAIL_FAIL, null);
        }
    }
}
