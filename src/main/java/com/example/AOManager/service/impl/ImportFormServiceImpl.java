package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.dto.manager.ImportFormDisplayDto;
import com.example.AOManager.entity.ImportDetailEntity;
import com.example.AOManager.entity.ImportFormEntity;
import com.example.AOManager.entity.OrderSupplierEntity;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.repository.*;
import com.example.AOManager.request.CreateImportFormRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.ImportFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.AOManager.common.Message.*;

@Service
public class ImportFormServiceImpl implements ImportFormService {

    @Autowired
    ImportFormRepository importFormRepository;

    @Autowired
    ImportDetailRepository importDetailRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OrderSupplierRepository orderSupplierRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public ApiResponse<?> getImportForm(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            ImportFormEntity importFormEntity = this.importFormRepository.findById(UUID.fromString(id)).get();
            ImportFormDisplayDto importFormDisplayDto = new ImportFormDisplayDto();
            importFormDisplayDto.setId(importFormEntity.getId().toString());
            importFormDisplayDto.setOrderSupplierId(importFormEntity.getOrderSupplierId().getId().toString());
            importFormDisplayDto.setCreateDate(importFormEntity.getCreateDate());
            importFormDisplayDto.setSupplierName(importFormEntity.getOrderSupplierId().getSupplierName());
            importFormDisplayDto.setEmployeeName(importFormEntity.getEmployeeId().getLastName() + " " + importFormEntity.getEmployeeId().getFirstName());
            List<ImportFormDisplayDto.Product> productList = new ArrayList<>();
            for (ImportDetailEntity importDetailEntity : importFormEntity.getImportDetailList()) {
                ImportFormDisplayDto.Product product = new ImportFormDisplayDto.Product();
                product.setProductId(importDetailEntity.getProductId().getId().toString());
                if (importDetailEntity.getProductId().getProductImageList().size() > 0) {
                    product.setProductImage(importDetailEntity.getProductId().getProductImageList().get(0).getUrl());
                } else {
                    product.setProductImage(null);
                }
                product.setName(importDetailEntity.getProductId().getName());
                product.setQuantity(importDetailEntity.getQuantity());
                product.setInventoryQuantity(importDetailEntity.getProductId().getInventoryQuantity());
                product.setUnitPrice(importDetailEntity.getPrice());
                product.setTotalPrice(importDetailEntity.getQuantity() * importDetailEntity.getPrice());
                productList.add(product);
            }
            importFormDisplayDto.setProductsList(productList);
            importFormDisplayDto.setTotalPriceImportForm(productList.stream().mapToLong(ImportFormDisplayDto.Product::getTotalPrice).sum());
            importFormDisplayDto.setCreateAt(null != importFormEntity.getCreatedAt() ? Function.toLongFromTimeStamp(importFormEntity.getCreatedAt()) : 0);
            importFormDisplayDto.setUpdateAt(null != importFormEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(importFormEntity.getUpdatedAt()) : 0);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_IMPORT_FORM_SUCCESS, importFormDisplayDto);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_IMPORT_FORM_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getImportFormList(int page, int limit, String keyWord) {
        if (0 >= page || 0 >= limit) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            long totalResult = this.importFormRepository.getCountRecord(keyWord).get().size();
            int totalPage = (int) Math.ceil((float) totalResult / limit);
            if (page > totalPage && totalPage != 0) {
                page = 1;
            }
            List<ImportFormEntity> importFormEntityList = this.importFormRepository.getImportFormList((page - 1) * limit, limit, keyWord).get();
            List<ImportFormDisplayDto> importFormDisplayDtoList = new ArrayList<>();
            for (ImportFormEntity importFormEntity : importFormEntityList) {
                ImportFormDisplayDto importFormDisplayDto = new ImportFormDisplayDto();
                importFormDisplayDto.setTotalPriceImportForm(0);
                importFormDisplayDto.setProductsList(null);
                importFormDisplayDto.setId(importFormEntity.getId().toString());
                importFormDisplayDto.setOrderSupplierId(importFormEntity.getOrderSupplierId().getId().toString());
                importFormDisplayDto.setCreateDate(importFormEntity.getCreateDate());
                importFormDisplayDto.setSupplierName(importFormEntity.getOrderSupplierId().getSupplierName());
                importFormDisplayDto.setEmployeeName(importFormEntity.getEmployeeId().getLastName() + " " + importFormEntity.getEmployeeId().getFirstName());
                importFormDisplayDto.setCreateAt(null != importFormEntity.getCreatedAt() ? Function.toLongFromTimeStamp(importFormEntity.getCreatedAt()) : 0);
                importFormDisplayDto.setUpdateAt(null != importFormEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(importFormEntity.getUpdatedAt()): 0);
                importFormDisplayDtoList.add(importFormDisplayDto);
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_IMPORT_FORM_LIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, importFormDisplayDtoList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_IMPORT_FORM_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> createImportForm(CreateImportFormRequest createImportFormRequest) {
        int totalQuantity = createImportFormRequest.getImportDetailList().stream()
                .mapToInt(CreateImportFormRequest.ImportDetail::getQuantity)
                .sum();
        if (totalQuantity <= 0) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_CANT_NOT_IMPORT_ZERO_QUANTITY, null);
        }
        ImportFormEntity importFormEntity;
        try {
            ImportFormEntity importFormToAdd = new ImportFormEntity();
            importFormToAdd.setCreateDate(createImportFormRequest.getCreateDate());
            importFormToAdd.setEmployeeId(this.usersRepository.findById(UUID.fromString(createImportFormRequest.getEmployeeId())).get());
            importFormToAdd.setOrderSupplierId(this.orderSupplierRepository.findById(UUID.fromString(createImportFormRequest.getOrderSupplierId())).get());
            importFormEntity = this.importFormRepository.save(importFormToAdd);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CREATE_IMPORT_FORM_FAIL, null);
        }
        try {
            for (CreateImportFormRequest.ImportDetail importDetail : createImportFormRequest.getImportDetailList()) {
                if (importDetail.getQuantity() > 0) {
                    ProductEntity productEntity = this.productRepository.findById(UUID.fromString(importDetail.getProductId())).get();
                    ImportDetailEntity importDetailEntity = new ImportDetailEntity();
                    importDetailEntity.setPrice(importDetail.getPrice());
                    importDetailEntity.setQuantity(importDetail.getQuantity());
                    importDetailEntity.setImportId(importFormEntity);
                    importDetailEntity.setProductId(productEntity);
                    this.importDetailRepository.save(importDetailEntity);
                    productEntity.setInventoryQuantity(productEntity.getInventoryQuantity() + importDetail.getQuantity());
                    productEntity.setStatus(productEntity.getInventoryQuantity() > 0 ? true : false);
                    this.productRepository.save(productEntity);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ADD_PRODUCT_FOR_IMPORT_FROM_FAIL, null);
        }
        OrderSupplierEntity orderSupplierEntity = this.orderSupplierRepository.findById(UUID.fromString(createImportFormRequest.getOrderSupplierId())).get();
        orderSupplierEntity.setStatus("IMPORTED");
        this.orderSupplierRepository.save(orderSupplierEntity);
        return new ApiResponse<>(HttpStatus.OK.value(), MSG_CREATE_IMPORT_FORM_SUCCESS, null);
    }
}
