package com.example.AOManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    private String id;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
