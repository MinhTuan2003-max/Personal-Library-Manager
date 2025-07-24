package com.luv2code.springboot.demo.plm.validation.validator;

import com.luv2code.springboot.demo.plm.model.enums.Genre;
import com.luv2code.springboot.demo.plm.validation.ValidGenre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenreValidator implements ConstraintValidator<ValidGenre, Genre> {

    @Override
    public boolean isValid(Genre value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.asList(Genre.values()).contains(value);
    }
}