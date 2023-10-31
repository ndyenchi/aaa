package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.dto.customer.ProductDisplayDto;
import com.example.AOManager.dto.manager.ProductDto;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.entity.ProductImageEntity;
import com.example.AOManager.repository.*;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.AOManager.common.Message.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    ImportDetailRepository importDetailRepository;

    @Autowired
    OrderSupplierDetailRepository orderSupplierDetailRepository;

    @Autowired
    PriceDetailRepository priceDetailRepository;

    @Autowired
    DeductionRepository deductionRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Override
    public long getTotalRecord(String categoryId, String keyWord) {
        if (CheckInput.isValidUUID(categoryId)) {
            return this.productRepository.getCountRecordWithCategory(UUID.fromString(categoryId), keyWord).get().size();
        } else {
            return this.productRepository.getCountRecord(keyWord).get().size();
        }
    }

    @Override
    public List<ProductDto> getProductsList(String categoryId, int page, int limit, String keyWord) {
        List<ProductEntity> productsList;
        if (CheckInput.isValidUUID(categoryId)) {
            productsList = this.productRepository.getProductsListWithCategory(UUID.fromString(categoryId), (page - 1) * limit, limit, keyWord).get();
        } else {
            productsList = this.productRepository.getProductsList((page - 1) * limit, limit, keyWord).get();
        }
        return productsList.stream().map(ProductDto::new).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<?> getProduct(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            ProductDto productDto = new ProductDto(this.productRepository.findById(UUID.fromString(id)).get());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCT_SUCCESS, productDto);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCT_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> createProduct(ProductDto productDto) {
        if (null == productDto || !CheckInput.isValidUUID(productDto.getCategoryId()) || false == CheckInput.checkProduct(productDto)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        if (this.productRepository.existsByName(Function.normalizeName(productDto.getName()))) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NAME_PRODUCT_EXIST, null);
        }
        ProductEntity productEntity = productDto.toEntity();
        productEntity.setInventoryQuantity(0);
        productEntity.setStatus(false);
        productEntity.setCategoryId(this.categoryRepository.findById(UUID.fromString(productDto.getCategoryId())).get());
        try {
            ProductEntity productEntityCreate = this.productRepository.save(productEntity);
            for (String productImage : productDto.getImageList()) {
                ProductImageEntity productImageEntity = new ProductImageEntity();
                productImageEntity.setUrl(productImage);
                productImageEntity.setProductId(productEntityCreate);
                try {
                    this.productImageRepository.save(productImageEntity);
                } catch (Exception e) {
                    System.out.println(e);
                    return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ADD_PICTURE_PRODUCT_FAIL, null);
                }
            }
            return new ApiResponse<>(HttpStatus.CREATED.value(), MSG_CREATE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CREATE_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> updateProduct(ProductDto productDto) {
        if (null == productDto || false == CheckInput.checkProduct(productDto)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            ProductEntity productEntityBef;
            try {
                productEntityBef = this.productRepository.findById(UUID.fromString(productDto.getId())).get();
                if (!productEntityBef.getName().equals(Function.normalizeName(productDto.getName()))) {
                    if (this.productRepository.existsByName(Function.normalizeName(productDto.getName()))) {
                        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NAME_PRODUCT_EXIST, null);
                    }
                }
            } catch (Exception e) {
                return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
            }
            ProductEntity productEntityAft = productDto.toEntity();
            productEntityAft.setStatus(productDto.getStatus());
            if (false == CheckInput.checkChangeStatusProduct(productEntityBef, productDto.getStatus())) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
            }
            productEntityAft.setStatus(productDto.getStatus());
            productEntityAft.setCategoryId(this.categoryRepository.findById(UUID.fromString(productDto.getCategoryId())).get());
            ProductEntity productEntityUpdated = this.productRepository.save(productEntityAft);
            List<ProductImageEntity> listDelete = this.productImageRepository.findByProductId_Id(productEntityUpdated.getId()).get();
            this.productImageRepository.deleteAll(listDelete);
            System.out.println(listDelete);
            for (String productImage : productDto.getImageList()) {
                ProductImageEntity productImageEntity = new ProductImageEntity();
                productImageEntity.setUrl(productImage);
                productImageEntity.setProductId(productEntityUpdated);
                try {
                    this.productImageRepository.save(productImageEntity);
                } catch (Exception e) {
                    System.out.println(e);
                    return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_PICTURE_PRODUCT_FAIL, null);
                }
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_UPDATE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> deleteProduct(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        ProductEntity productEntity;
        try {
            productEntity = this.productRepository.findById(UUID.fromString(id)).get();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
        }
        if (this.importDetailRepository.existsByProductId_Id(productEntity.getId())) {
            productEntity.setStatus(false);
            this.productRepository.save(productEntity);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRODUCT_FAIL_IMPORT, null);
        } else if (this.orderSupplierDetailRepository.existsByProductId_Id(productEntity.getId())) {
            productEntity.setStatus(false);
            this.productRepository.save(productEntity);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRODUCT_FAIL_ORDER_SUPPLIER, null);
        } else if (this.priceDetailRepository.existsByProductId_Id(productEntity.getId())) {
            productEntity.setStatus(false);
            this.productRepository.save(productEntity);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRODUCT_FAIL_PRICE, null);
        } else if (this.deductionRepository.existsByProductId_Id(productEntity.getId())) {
            productEntity.setStatus(false);
            this.productRepository.save(productEntity);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRODUCT_FAIL_DEDUCTION, null);
        } else if (this.cartDetailRepository.existsByProductId_Id(productEntity.getId())) {
            productEntity.setStatus(false);
            this.productRepository.save(productEntity);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_PRODUCT_FAIL_CART, null);
        } else {
            try {
                List<ProductImageEntity> productImageEntityList = this.productImageRepository.findByProductId_Id(productEntity.getId()).get();
                this.productImageRepository.deleteAll(productImageEntityList);
                this.productRepository.delete(productEntity);
                return new ApiResponse<>(HttpStatus.OK.value(), MSG_DELETE_SUCCESS, null);
            } catch (Exception e) {
                System.out.println(e);
                return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_FAIL, null);
            }
        }
    }

    @Override
    public ApiResponse<?> getProductsListForCustomer(String categoryId, String orderByPrice, int limit, String keyWord) {
        List<ProductDisplayDto> productDisplayDtoList;
        try {
            List<ProductEntity> productEntityList;
            long total;
            if (CheckInput.isValidUUID(categoryId)) {
                productEntityList = this.productRepository.getProductsListForCustomerWithCategory(UUID.fromString(categoryId), keyWord).get();
            } else {
                productEntityList = this.productRepository.getProductsListForCustomerWithoutCategory(keyWord).get();
            }
            productEntityList = Function.removeProductWithNoPrice(productEntityList);
            if(orderByPrice.equals("ASC") && productEntityList.size() > 0) {
                productEntityList.sort(Comparator.comparingLong(ProductEntity::getCurrentPrice));
            } else if(orderByPrice.equals("DESC") && productEntityList.size() > 0)  {
                productEntityList.sort((a1, a2) -> -Long.compare(a1.getCurrentPrice(), a2.getCurrentPrice()));
            }
            total = productEntityList.size();
            productEntityList = productEntityList.subList(0, Math.min(limit, productEntityList.size()));
            productDisplayDtoList = productEntityList.stream().map(ProductDisplayDto::new).collect(Collectors.toList());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCTS_lIST_SUCCESS, new ApiResponseForList<>(total, null, null, null, productDisplayDtoList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCTS_lIST_FAIL, null);
        }
    }
}
