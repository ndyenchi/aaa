package com.example.AOManager.service.impl;

import com.example.AOManager.entity.RoleEntity;
import com.example.AOManager.entity.UserRoleEntity;
import com.example.AOManager.entity.UsersEntity;
import com.example.AOManager.payload.request.ChangePasswordRequest;
import com.example.AOManager.payload.request.LoginRequest;
import com.example.AOManager.payload.request.UserSignupRequest;
import com.example.AOManager.request.ResetPasswordRequest;
import com.example.AOManager.response.ApiResponse;
import com.example.AOManager.payload.response.JwtResponse;
import com.example.AOManager.repository.RoleRepository;
import com.example.AOManager.repository.UserRoleRepository;
import com.example.AOManager.repository.UsersRepository;
import com.example.AOManager.security.jwt.JwtUtils;
import com.example.AOManager.security.services.UserDetailsImpl;
import com.example.AOManager.service.AuthService;
import com.example.AOManager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.AOManager.common.CheckInput.stringIsNullOrEmpty;
import static com.example.AOManager.common.Message.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    @Lazy
    UsersService usersService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 10;

    @Override
    public ApiResponse sigin(LoginRequest loginRequest) {
        if (stringIsNullOrEmpty(loginRequest.getEmail()) || stringIsNullOrEmpty(loginRequest.getPassword())) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl employeeDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = employeeDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new ApiResponse(HttpStatus.OK.value(), MSG_LOGIN_SUCCESS, new JwtResponse(jwt, employeeDetails.getUsername(), roles));
    }

    @Override
    public ApiResponse<?> signup(UserSignupRequest signupRequest) {
        if (this.usersRepository.existsByEmail(signupRequest.getEmail())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_EMAIL_EXIST, null);
        }
        UsersEntity user = new UsersEntity();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setBirthday(signupRequest.getBirthday());
        user.setGender(signupRequest.getGender());
        user.setAddress(signupRequest.getAddress());
        user.setPhone(signupRequest.getPhone());
        user.setStatus(true);
        try {
            this.usersRepository.save(user);
            UsersEntity userRegistry = this.usersRepository.findByEmail(signupRequest.getEmail()).get();
            RoleEntity roleRegistry = this.roleRepository.findById(UUID.fromString(signupRequest.getRoleId())).get();
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(userRegistry);
            userRole.setRoleId(roleRegistry);
            this.userRoleRepository.save(userRole);
            return new ApiResponse<>(HttpStatus.CREATED.value(), MSG_REGISTRY_SUCCESS, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_REGISTRY_FAIL, null);
        }
    }

    @Override
    public ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        return this.usersService.changePassword(changePasswordRequest);
    }

    public String forgotPassword(String email) {
        UsersEntity userOptional = this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản tương ứng với emai này: " + email));
        userOptional.setToken(generateToken());
        userOptional.setTokenCreationDate(LocalDateTime.now());
        userOptional = this.usersRepository.save(userOptional);
        return userOptional.getToken();
    }

    private String generateToken() {
        String token = UUID.randomUUID().toString().replace("-", "");
        return token;
    }

    @Override
    public ApiResponse<?> sendEmail(String email) throws MessagingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true);
        String response = forgotPassword(email);
        String link = "http://localhost:4200/reset-password/" + response;
        String content = "<p>Xin chào,</p>"
                + "<p>Click vào đường dẫn sau để đặt lại mật khẩu cho tài khoản của bạn: </p>"
                + "<p><a href=\"" + link + "\">Reset password</a></p>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, bạn có thể bỏ qua thông báo này!"
                + "<br>"
                + "Cảm ơn!</p>";
        helper.setTo(email);
        helper.setSubject("ĐẶT LẠI MẬT KHẨU");
        helper.setText(content, true);
        javaMailSender.send(mimeMailMessage);
        return new ApiResponse<>(HttpStatus.OK.value(), MSG_SENT_MAIL_SUCCESS, response);
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);
        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    @Override
    public ApiResponse<?> resetPassword(ResetPasswordRequest request) {
        Optional<UsersEntity> userOptional = this.usersRepository.findByToken(request.getToken());
        if (!userOptional.isPresent()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_BAD_REQUEST, null);
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), MSG_TOKEN_EXPIRED, null);
        }
        UsersEntity user = userOptional.get();
        user.setPassword(encoder.encode(request.getPassword()));
        user.setToken(null);
        user.setTokenCreationDate(null);
        this.usersRepository.save(user);
        return new ApiResponse<>(HttpStatus.OK.value(), MSG_RESET_PASSWORD_SUCCESS, null);
    }
}
