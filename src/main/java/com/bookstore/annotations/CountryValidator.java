package com.bookstore.annotations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CountryValidator implements ConstraintValidator<CountryValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isCountryValid(value);
    }

    public static boolean isCountryValid(String value) {
        List<String> listOfCountries = Arrays.asList(Locale.getISOCountries());
        return listOfCountries.contains(value);
    }

}
