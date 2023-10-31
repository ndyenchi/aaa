package com.example.AOManager.controller.customer;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer/home")
public class HomeController {

    @Autowired
    HomeService homeService;

    @GetMapping("/new-products")
    ApiResponse<?> getNewProductsList() {return this.homeService.getNewProductsList();}

    @GetMapping("/best-selling-products")
    ApiResponse<?> getBestSellingProductsList() {return this.homeService.getBestSellingProductsList();}
}
