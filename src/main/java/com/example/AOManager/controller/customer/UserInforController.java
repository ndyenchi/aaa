package com.example.AOManager.controller.customer;

import com.example.AOManager.payload.request.ChangeInforCustomerRequest;
import com.example.AOManager.repository.UsersRepository;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer/user-infor")
public class UserInforController {

    @Autowired
    UsersService usersService;

    @GetMapping("/{userId}")
    ApiResponse<?> getInforCustomer(@PathVariable String userId) {return this.usersService.getUser(userId);}

    @PutMapping
    ApiResponse<?> changeInforCustomer(@RequestBody ChangeInforCustomerRequest changeInforCustomerRequest) {return this.usersService.changeInforCustomer(changeInforCustomerRequest);}
}
