package com.example.AOManager.controller.manager;

import com.example.AOManager.request.CreateOrderSupplierRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.OrderSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order-suppliers")
public class OrderSupplierController {

    @Autowired
    OrderSupplierService orderSupplierService;

    @GetMapping("/{id}")
    public ApiResponse<?> getOrderSupplier(@PathVariable String id) {return this.orderSupplierService.getOrderSupplier(id);}

    @GetMapping
    public ApiResponse<?> getOrderSupplierList(@RequestParam String status, @RequestParam int page, @RequestParam int limit, @RequestParam String keyWord) {return this.orderSupplierService.getOrderSupplierList(status, page, limit, keyWord);}

    @PostMapping
    public ApiResponse<?> createOrderSupplier(@RequestBody CreateOrderSupplierRequest createOrderSupplierRequest) {return this.orderSupplierService.createOrderSupplier(createOrderSupplierRequest);}

    @PutMapping("/{id}")
    public ApiResponse<?> cancelOrderSupplier(@PathVariable String id) {return this.orderSupplierService.cancelOrderSupplier(id);}
}
