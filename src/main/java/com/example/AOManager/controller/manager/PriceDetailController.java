package com.example.AOManager.controller.manager;

import com.example.AOManager.request.CreatePriceDetailRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.PriceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/price-details")
public class PriceDetailController {

    @Autowired
    PriceDetailService priceDetailService;

    @GetMapping("/{id}")
    ApiResponse<?> getPriceDetail (@PathVariable String id) {return this.priceDetailService.getPriceDetail(id);}

    @GetMapping
    ApiResponse<?> getPriceDetailsList(@RequestParam String productId) {return this.priceDetailService.getPriceDetailsList(productId);}

    @PostMapping
    ApiResponse<?> createPriceDetail(@RequestBody CreatePriceDetailRequest createPriceDetailRequest) {return this.priceDetailService.createPriceDetail(createPriceDetailRequest);}

    @DeleteMapping("/{id}")
    ApiResponse<?> deletePriceDetail(@PathVariable String id) {return this.priceDetailService.deletePriceDetail(id);}
}
