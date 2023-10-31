package com.example.AOManager.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseForList<T> {
    private Long totalResult;
    private Integer currentPage;
    private Integer totalPage;
    private Integer numPerPage;
    private T data;
}
