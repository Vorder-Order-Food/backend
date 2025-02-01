package com.vorder.identity_service.controller;


import com.vorder.identity_service.dto.ApiResponse;
import com.vorder.identity_service.dto.UserDto;
import com.vorder.identity_service.dto.request.UserCreationRequest;
import com.vorder.identity_service.dto.request.UserUpdateRequest;
import com.vorder.identity_service.dto.request.VerifyEmailRequest;
import com.vorder.identity_service.dto.response.UserResponse;
import com.vorder.identity_service.entity.User;
import com.vorder.identity_service.mapper.UserMapper;
import com.vorder.identity_service.service.AuthenticationService;
import com.vorder.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;
    AuthenticationService authenticationService;
    UserMapper userMapper;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }


    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String userId = authenticationService.getUserIdFromToken(token);

        VerifyEmailRequest verifyEmailRequest = new VerifyEmailRequest();
        verifyEmailRequest.setEmailVerified(true);
        userService.verifyEmail(userId, verifyEmailRequest);

        return ResponseEntity.ok("Email verified successfully!");
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/get-user")
    ApiResponse<UserResponse> getUser() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser())
                .build();
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
