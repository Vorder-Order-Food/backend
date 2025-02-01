package com.vorder.identity_service.mapper;


import com.vorder.identity_service.dto.UserDto;
import com.vorder.identity_service.dto.request.UserCreationRequest;
import com.vorder.identity_service.dto.request.UserUpdateRequest;
import com.vorder.identity_service.dto.request.VerifyEmailRequest;
import com.vorder.identity_service.dto.response.UserResponse;
import com.vorder.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    UserDto toUserDto(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    void verifyUserEmail(@MappingTarget User user, VerifyEmailRequest request);
}
