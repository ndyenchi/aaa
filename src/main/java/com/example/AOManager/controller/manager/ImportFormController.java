package com.example.AOManager.controller.manager;

import com.example.AOManager.request.CreateImportFormRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.service.ImportFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/import-forms")
public class ImportFormController {

    @Autowired
    ImportFormService importFormService;

    @GetMapping("/{id}")
    public ApiResponse<?> getImportForm(@PathVariable String id) {return this.importFormService.getImportForm(id);}

    @GetMapping
    public ApiResponse<?> getImportFormList(@RequestParam int page, @RequestParam int limit, @RequestParam String keyWord) {return this.importFormService.getImportFormList(page, limit, keyWord);}

    @PostMapping
    public ApiResponse<?> createImportForm(@RequestBody CreateImportFormRequest createImportFormRequest) {return this.importFormService.createImportForm(createImportFormRequest);}
}
