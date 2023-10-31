package com.example.AOManager.service;

import com.example.AOManager.entity.UsersEntity;
import com.example.AOManager.payload.request.ChangeInforCustomerRequest;
import com.example.AOManager.payload.request.ChangePasswordRequest;
import com.example.AOManager.request.UserRequest;
import com.example.AOManager.response.ApiResponse;

import java.util.List;

public interface UsersService {

    ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest);

    ApiResponse<?> getUser(String id);

    List<UsersEntity> getUsersList(String roleId, int page, int limit, String keyWord);

    ApiResponse<?> getCustomerList(String roleName, int page, int limit, String keyWord);

    ApiResponse<?> getEmployeeList(String roleId, int page, int limit, String keyWord);

    ApiResponse<?> getManagerList(String roleId, int page, int limit, String keyWord);

    ApiResponse<?> createUser(UserRequest createUserRequest, String role);

    ApiResponse<?> updateUser(UserRequest updateUserRequest);

    ApiResponse<?> changeInforCustomer(ChangeInforCustomerRequest changeInforCustomerRequest);
}
