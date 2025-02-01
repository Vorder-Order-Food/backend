package com.vorder.product_service.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Builder
@Document(value = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @MongoId
    String id;

    String name;
    Long price;
    List<String> images;
}
