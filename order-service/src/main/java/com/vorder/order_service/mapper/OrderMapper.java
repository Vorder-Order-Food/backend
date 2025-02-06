package com.vorder.order_service.mapper;

import com.vorder.order_service.dto.OrderDto;
import com.vorder.order_service.dto.UserDto;
import com.vorder.order_service.dto.response.AdminOrderResponse;
import com.vorder.order_service.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "order.id", target = "id")
    AdminOrderResponse toResponse(Order order, UserDto user);

    OrderDto toDto(Order order);

}
