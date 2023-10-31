package com.example.AOManager.service;

import com.example.AOManager.request.CreateImportFormRequest;
import com.example.AOManager.response.ApiResponse;

public interface ImportFormService {

    ApiResponse<?> createImportForm(CreateImportFormRequest createImportFormRequest);

    ApiResponse<?> getImportForm(String id);

    ApiResponse<?> getImportFormList(int page, int limit, String keyWord);
}
