package com.example.AOManager.controller.manager;

import com.example.AOManager.dto.manager.ProductDto;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.response.ApiResponseForList;
import com.example.AOManager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.example.AOManager.common.Message.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ApiResponse<?> getProduct(@PathVariable String id) {return this.productService.getProduct(id);}

    @GetMapping
    public ApiResponse<?> getProductsList(@RequestParam String categoryId, @RequestParam String orderByPrice, @RequestParam int page, @RequestParam int limit, @RequestParam String keyWord) {
         try {
             long totalResult = this.productService.getTotalRecord(categoryId, keyWord);
             int totalPage = (int) Math.ceil((float)totalResult/limit);
             if(page > totalPage && totalPage != 0) {
                 page = 1;
             }
             List<ProductDto> productsList = this.productService.getProductsList(categoryId, page, limit, keyWord);
             if(orderByPrice.equals("ASC") && productsList.size() > 0) {
                 productsList.sort(Comparator.comparingLong(ProductDto::getPrice));
             } else if(orderByPrice.equals("DESC") && productsList.size() > 0)  {
                 productsList.sort((a1, a2) -> -Long.compare(a1.getPrice(), a2.getPrice()));
             }
             return new ApiResponse<>(HttpStatus.OK.value(), MSG_GET_PRODUCTS_lIST_SUCCESS, new ApiResponseForList<>(totalResult, page, totalPage, limit, productsList));
         } catch (Exception e) {
             System.out.println(e);
             return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_GET_PRODUCTS_lIST_FAIL, null);
         }
    }

    @PostMapping
    public ApiResponse<?> createProduct(@RequestBody ProductDto productDto) {return this.productService.createProduct(productDto);}

    @PutMapping()
    public ApiResponse<?> updateProduct(@RequestBody ProductDto productDto) {return this.productService.updateProduct(productDto);}

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteProduct(@PathVariable String id) {return this.productService.deleteProduct(id);}
}
