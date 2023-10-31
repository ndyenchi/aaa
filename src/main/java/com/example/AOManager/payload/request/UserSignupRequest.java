package com.example.AOManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private long birthday;
    private String gender;
    private String address;
    private String phone;
    private String roleId;
}
