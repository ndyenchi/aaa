package com.example.AOManager.service.impl;

import com.example.AOManager.common.CheckInput;
import com.example.AOManager.common.Function;
import com.example.AOManager.dto.manager.CustomerDisplayDto;
import com.example.AOManager.dto.manager.EmployeeDisplayDto;
import com.example.AOManager.dto.manager.UsersDto;
import com.example.AOManager.entity.UserRoleEntity;
import com.example.AOManager.entity.UsersEntity;
import com.example.AOManager.payload.request.ChangeInforCustomerRequest;
import com.example.AOManager.payload.request.ChangePasswordRequest;
import com.example.AOManager.repository.RoleRepository;
import com.example.AOManager.repository.UserRoleRepository;
import com.example.AOManager.request.UserRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.repository.UsersRepository;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.AOManager.common.Message.*;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    @Lazy
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        UsersEntity user = this.usersRepository.findById(UUID.fromString(changePasswordRequest.getId())).get();
        if (!encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_OLD_PASSWORD_NOT_TRUE, null); // mật khẩu cũ không khớp
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getNewPasswordConfirm())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_NEW_PASSWORD_NOT_MATCH, null); // Mật khẩu mới xác nhận lại không khớp
        }
        user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
        try {
            this.usersRepository.save(user);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_CHANGE_PASSWORD_SUCCESS, null); // thành công
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CHANGE_PASSWORD_FAIL, null); // thất bại
        }
    }

    @Override
    public ApiResponse<?> getUser(String id) {
        try {
            UsersEntity usersEntity = this.usersRepository.findById(UUID.fromString(id)).get();
            usersEntity.setPassword(this.encoder.encode(usersEntity.getPassword()));
            UsersDto usersDto = new UsersDto(usersEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_USER_SUCCESS, usersDto);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_USER_FAIL, null);
        }
    }

    @Override
    public List<UsersEntity> getUsersList(String roleId, int page, int limit, String keyWord) {
        List<UsersEntity> managerList = this.usersRepository.getUsersList(UUID.fromString(roleId), (page - 1) * limit, limit, keyWord);
        return managerList;
    }

    @Override
    public ApiResponse<?> getCustomerList(String roleName, int page, int limit, String keyWord) {
        try {
            String roleId = this.roleRepository.findByName(roleName).get().getId().toString();
            long totalResult = this.userRoleRepository.getCountRecord(UUID.fromString(roleId), keyWord).get().size();
            int totalPage = (int) Math.ceil((float) totalResult / limit);
            if (page > totalPage && totalPage != 0) {
                page = 1;
            }
            List<UsersEntity> usersEntityList = this.usersService.getUsersList(roleId, page, limit, keyWord);
            List<CustomerDisplayDto> customerDisplayDtoList = new ArrayList<>();
            for (UsersEntity UsersEntity : usersEntityList) {
                customerDisplayDtoList.add(new CustomerDisplayDto(UsersEntity));
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_CUSTOMER_LIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, customerDisplayDtoList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_CUSTOMER_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getEmployeeList(String roleId, int page, int limit, String keyWord) {
        try {
            long totalResult = this.userRoleRepository.getCountRecord(UUID.fromString(roleId), keyWord).get().size();
            int totalPage = (int) Math.ceil((float) totalResult / limit);
            if (page > totalPage && totalPage != 0) {
                page = 1;
            }
            List<UsersEntity> usersEntityList = this.usersService.getUsersList(roleId, page, limit, keyWord);
            List<EmployeeDisplayDto> employeeList = new ArrayList<>();
            for (UsersEntity usersEntity : usersEntityList) {
                employeeList.add(new EmployeeDisplayDto(usersEntity));
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_EMPLOYEE_LIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, employeeList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_EMPLOYEE_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> getManagerList(String roleId, int page, int limit, String keyWord) {
        try {
            long totalResult = this.userRoleRepository.getCountRecord(UUID.fromString(roleId), keyWord).get().size();
            int totalPage = (int) Math.ceil((float) totalResult / limit);
            if (page > totalPage && totalPage != 0) {
                page = 1;
            }
            List<UsersEntity> usersEntityList = this.usersService.getUsersList(roleId, page, limit, keyWord);
            List<EmployeeDisplayDto> managerList = new ArrayList<>();
            for (UsersEntity UsersEntity : usersEntityList) {
                managerList.add(new EmployeeDisplayDto(UsersEntity));
            }
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_MANAGER_LIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, managerList));
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_MANAGER_LIST_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> createUser(UserRequest createUserRequest, String role) {
        if (this.usersRepository.existsByEmail(createUserRequest.getEmail())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_EMAIL_EXIST, null);
        }
        try {
            createUserRequest.setStatus(true);
            createUserRequest.setPassword(this.encoder.encode(createUserRequest.getPassword()));
            UsersEntity usersEntity = createUserRequest.toEntity();
            usersEntity.setEmail(createUserRequest.getEmail());
            usersEntity.setPassword(createUserRequest.getPassword());
            UsersEntity usersEntityCreated = this.usersRepository.save(usersEntity);
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(usersEntityCreated);
            userRoleEntity.setRoleId(this.roleRepository.findByName(role).get());
            this.userRoleRepository.save(userRoleEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_CREATE_USER_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_CREATE_USER_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> updateUser(UserRequest updateUserRequest) {
        try {
            UsersEntity usersEntityFind;
            try {
                usersEntityFind = this.usersRepository.findById(UUID.fromString(updateUserRequest.getId())).get();
            } catch (Exception e) {
                System.out.println(e);
                return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), MSG_NOT_FOUND_BY_ID, null);
            }
            UsersEntity usersEntity = updateUserRequest.toEntity();
            usersEntity.setId(UUID.fromString(updateUserRequest.getId()));
            usersEntity.setEmail(usersEntityFind.getEmail());
            usersEntity.setPassword(usersEntityFind.getPassword());
            this.usersRepository.save(usersEntity);
            UserRoleEntity userRoleEntity = this.userRoleRepository.findByUserId_Id(UUID.fromString(updateUserRequest.getId())).get();
            userRoleEntity.setRoleId(this.roleRepository.findById(UUID.fromString(updateUserRequest.getRoleId())).get());
            this.userRoleRepository.save(userRoleEntity);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_UPDATE_USER_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_USER_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> changeInforCustomer(ChangeInforCustomerRequest request) {
        if (!CheckInput.isValidName(request.getFirstName()) || !CheckInput.isValidName(request.getLastName())
        || !CheckInput.isValidDate(request.getBirthday()) || !CheckInput.isValidPhoneNumber(request.getPhone())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        try {
            UsersEntity customer = this.usersRepository.findById(UUID.fromString(request.getCustomerId())).get();
            customer.setFirstName(Function.normalizeName(request.getFirstName()));
            customer.setLastName(Function.normalizeName(request.getLastName()));
            customer.setGender(request.getGender());
            customer.setBirthday(request.getBirthday());
            customer.setPhone(request.getPhone());
            customer.setAddress(request.getAddress());
            this.usersRepository.save(customer);
            return new ApiResponse<>(HttpStatus.OK.value(), MSG_UPDATE_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_UPDATE_FAIL, null);
        }
    }
}
