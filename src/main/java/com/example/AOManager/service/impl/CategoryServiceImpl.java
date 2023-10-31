package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.dto.manager.CategoryDisplayDto;
import com.example.AOManager.dto.manager.CategoryDto;
import com.example.AOManager.entity.CategoryEntity;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.repository.CategoryRepository;
import com.example.AOManager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.AOManager.common.Message.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ApiResponse<?> getAllCategoriesMaster() {
        try {
            List<CategoryEntity> categoryList = this.categoryRepository.findAll(Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("createdAt")));
            List<CategoryDisplayDto> categoryDtoList = new ArrayList<>();
            for (CategoryEntity categoryEntity : categoryList) {
                categoryDtoList.add(new CategoryDisplayDto(categoryEntity));
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_CATEGORIES_SUCCESS, categoryDtoList);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_CATEGORIES_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getCategory(String id) {
        if (CheckInput.stringIsNullOrEmpty(id) || !CheckInput.isValidUUID(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            CategoryDto categoryDto = new CategoryDto(this.categoryRepository.findById(UUID.fromString(id)).get());
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_CATEGORY_SUCCESS, categoryDto);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_CATEGORY_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> createCategory(String name) {
        if (CheckInput.stringIsNullOrEmpty(name)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        try {
            this.categoryRepository.save(categoryEntity);
            return new ApiResponse<>(HttpStatus.CREATED.value(), MSG_CREATE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CREATE_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> updateCategory(String id, String name) {
        if (CheckInput.stringIsNullOrEmpty(id) || CheckInput.stringIsNullOrEmpty(name)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            CategoryEntity categoryEntityBef;
            try {
                categoryEntityBef = this.categoryRepository.findById(UUID.fromString(id)).get();
            } catch (Exception e) {
                System.out.println(e);
                return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
            }
            CategoryEntity categoryEntityAft = new CategoryEntity(UUID.fromString(id), name, categoryEntityBef.getProductList());
            this.categoryRepository.save(categoryEntityAft);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_UPDATE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> deleteCategory(String id) {
        if (CheckInput.stringIsNullOrEmpty(id)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            CategoryEntity categoryEntity;
            try {
                categoryEntity = this.categoryRepository.findById(UUID.fromString(id)).get();
            } catch (Exception e) {
                System.out.println(e);
                return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
            }
            this.categoryRepository.delete(categoryEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_DELETE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_DELETE_FAIL, null);
        }
    }
}
