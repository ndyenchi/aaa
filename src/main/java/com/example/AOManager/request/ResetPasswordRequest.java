package com.example.AOManager.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
}
