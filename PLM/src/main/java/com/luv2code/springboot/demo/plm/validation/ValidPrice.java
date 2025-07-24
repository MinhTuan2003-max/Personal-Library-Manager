package com.luv2code.springboot.demo.plm.validation;

import com.luv2code.springboot.demo.plm.validation.validator.PriceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
public @interface ValidPrice {
    String message() default "Giá phải lớn hơn 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
