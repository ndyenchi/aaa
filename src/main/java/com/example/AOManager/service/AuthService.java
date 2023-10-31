package com.example.AOManager.service;

import com.example.AOManager.payload.request.ChangePasswordRequest;
import com.example.AOManager.payload.request.LoginRequest;
import com.example.AOManager.payload.request.UserSignupRequest;
import com.example.AOManager.request.ResetPasswordRequest;
import com.example.AOManager.response.ApiResponse;

import javax.mail.MessagingException;

public interface AuthService {

    ApiResponse sigin(LoginRequest loginRequest);

    ApiResponse<?> signup(UserSignupRequest signupRequest);

    ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest);

    ApiResponse<?> sendEmail(String email) throws MessagingException;

    ApiResponse<?> resetPassword(ResetPasswordRequest request);
}
