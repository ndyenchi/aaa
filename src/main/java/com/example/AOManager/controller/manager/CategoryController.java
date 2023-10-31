package com.example.AOManager.controller.manager;

import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ApiResponse<?> getCategory(@PathVariable String id) {return this.categoryService.getCategory(id);}

    @GetMapping
    public ApiResponse<?> getAllCategoriesList() {return this.categoryService.getAllCategoriesMaster();}

    @PostMapping("/{name}")
    public ApiResponse<?> createCategory(@PathVariable String name) {return this.categoryService.createCategory(name);}

    @PutMapping
    public ApiResponse<?> updateCategory(@RequestParam String id, @RequestParam String name) {return this.categoryService.updateCategory(id, name);}

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteCategory(@PathVariable String id) {return this.categoryService.deleteCategory(id);}
}
