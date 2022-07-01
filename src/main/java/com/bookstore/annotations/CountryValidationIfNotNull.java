package com.bookstore.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({ElementType.PARAMETER, TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CountryValidatorIfNotNull.class)
public @interface CountryValidationIfNotNull {
    //error message
    String message() default "Invalid country code. Use only ISO countries codes or leave field empty";

    //represents group of constraints
    Class<?>[] groups() default {};

    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
