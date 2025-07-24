package com.luv2code.springboot.demo.plm.model.dto.response;

import com.luv2code.springboot.demo.plm.model.enums.Genre;
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
public class BookResponse {
    private String id;
    private String title;
    private String author;
    private Genre genre;
    private String description;
    private BigDecimal price;
    private String coverImageUrl;
    private LocalDateTime createdAt;
}