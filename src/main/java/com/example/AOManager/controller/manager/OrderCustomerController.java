package com.example.AOManager.controller.manager;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.OrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order-customers")
public class OrderCustomerController {

    @Autowired
    OrderCustomerService orderCustomerService;

    @GetMapping("/{id}")
    public ApiResponse<?> getOrderCustomer(@PathVariable String id) {return this.orderCustomerService.getOrderCustomer(id);}

    @GetMapping
    public ApiResponse<?> getOrderCustomerList(@RequestParam String orderStatusId, @RequestParam int page, @RequestParam int limit, @RequestParam String keyWord) {return this.orderCustomerService.getOrderCustomerList(orderStatusId, page, limit, keyWord);}

    @PutMapping
    public ApiResponse<?> updateStatusForOrderCustomer(@RequestParam String orderStatusIdTo, @RequestParam String id) {return this.orderCustomerService.updateStatusForOrderCustomer(orderStatusIdTo, id);}
}
