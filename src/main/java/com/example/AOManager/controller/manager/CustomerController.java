package com.example.AOManager.controller.manager;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    UsersService usersService;

    @GetMapping("/{id}")
    public ApiResponse<?> getCustomer(@PathVariable String id) {return this.usersService.getUser(id);}

    @GetMapping
    public ApiResponse<?> getCustomerList(@RequestParam int page, @RequestParam int limit, @RequestParam String keyWord) {return this.usersService.getCustomerList("ROLE_CUSTOMER", page, limit, keyWord);}
}
