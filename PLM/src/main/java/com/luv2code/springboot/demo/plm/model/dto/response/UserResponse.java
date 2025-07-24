package com.luv2code.springboot.demo.plm.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private String picture;
    private BigDecimal budget;
    private BigDecimal totalSpent;
    private LocalDateTime createdAt;
}