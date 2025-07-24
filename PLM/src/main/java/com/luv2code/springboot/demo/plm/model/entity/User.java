package com.luv2code.springboot.demo.plm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private String email;
    private String name;
    private String picture;

    @Builder.Default
    private BigDecimal budget = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdAt;
}
