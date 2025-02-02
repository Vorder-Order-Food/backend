package com.vorder.identity_service.service;


import com.vorder.event.NotificationEvent;
import com.vorder.identity_service.constant.PredefinedRole;
import com.vorder.identity_service.dto.ApiResponse;
import com.vorder.identity_service.dto.request.CreateCartRequest;
import com.vorder.identity_service.dto.request.UserCreationRequest;
import com.vorder.identity_service.dto.request.UserUpdateRequest;
import com.vorder.identity_service.dto.request.VerifyEmailRequest;
import com.vorder.identity_service.dto.response.CreateCartResponse;
import com.vorder.identity_service.dto.response.UserResponse;
import com.vorder.identity_service.entity.Role;
import com.vorder.identity_service.entity.User;
import com.vorder.identity_service.exception.AppException;
import com.vorder.identity_service.exception.ErrorCode;
import com.vorder.identity_service.mapper.UserMapper;
import com.vorder.identity_service.repository.RoleRepository;
import com.vorder.identity_service.repository.UserRepository;
import com.vorder.identity_service.repository.httpclient.CartClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    CartClient cartClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    AuthenticationService authenticationService;
    HttpServletRequest httpServletRequest;
    EmailService emailService;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        CreateCartRequest req = CreateCartRequest.builder()
                .userId(user.getId())
                .build();
        try {
            log.info("Creating cart with request: {}", req);
            ApiResponse<CreateCartResponse> cartResponse = cartClient.createCart(req);
            log.info("Cart creation response: {}", cartResponse);
        } catch (Exception ex) {
            throw new AppException(ErrorCode.CART_CREATION_FAILED);
        }


        String token = authenticationService.generateToken(user);

        String verificationUrl = applicationUrl(httpServletRequest) + "/users/verify-email?token=" + token;
        String emailBody = emailService.generateEmailContent(request.getUsername(), verificationUrl);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to Vorder")
                .body(emailBody)
                .build();

        // Publish message to kafka
        kafkaTemplate.send("notification-send", notificationEvent);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }


    public UserResponse verifyEmail(String userId, VerifyEmailRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.verifyUserEmail(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userMapper.toUserResponse(
                userRepository.findById(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public User findUserById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
