package com.vorder.order_service.repository.http;

import com.vorder.order_service.configuration.AuthenticationRequestInterceptor;
import com.vorder.order_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${app.services.identity}",  configuration = { AuthenticationRequestInterceptor.class })
public interface UserClient {
    @GetMapping("/identity/users/user/{userId}")
    UserDto getUser(@PathVariable("userId") String userId);
}
