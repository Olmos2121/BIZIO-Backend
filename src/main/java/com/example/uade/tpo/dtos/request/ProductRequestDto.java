package com.example.uade.tpo.dtos.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequestDto {
    String name;
    String description;
    String brand;
    Double price;
    Integer stock;
    Long sellerId;
    String image;
}
