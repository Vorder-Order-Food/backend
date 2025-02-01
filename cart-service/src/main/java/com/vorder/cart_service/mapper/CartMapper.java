package com.vorder.cart_service.mapper;

import com.vorder.cart_service.dto.CartDto;
import com.vorder.cart_service.dto.request.CreateCartRequest;
import com.vorder.cart_service.dto.response.CreateCartResponse;
import com.vorder.cart_service.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CreateCartRequest request);

    CreateCartResponse toCreateCartResponse(Cart cart);
    CartDto toCartDto(Cart cart);
}
