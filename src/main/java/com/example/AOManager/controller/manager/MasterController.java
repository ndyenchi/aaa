package com.example.AOManager.controller.manager;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.CategoryService;
import com.example.AOManager.service.OrderStatusService;
import com.example.AOManager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    RoleService roleService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OrderStatusService orderStatusService;

    @GetMapping("/roles")
    public ApiResponse<?> getAllRole() {return this.roleService.getAllRoles();}

    @GetMapping("/categories")
    public ApiResponse<?> getAllCategoriesMaster() {return this.categoryService.getAllCategoriesMaster();}

    @GetMapping("/order-status")
    public ApiResponse<?> getAllOrderStatus() {return this.orderStatusService.getAllOrderStatus();}

    @GetMapping("/employee-roles")
    public ApiResponse<?> getEmployeeRoles() {return this.roleService.getEmployeoleRoles();}
}
