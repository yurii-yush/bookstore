package com.bookstore.annotations;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidatorIfNotNull implements ConstraintValidator<CountryValidationIfNotNull, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return StringUtils.isBlank(value) || CountryValidator.isCountryValid(value);
    }
}
