package dev.noah.word.config.constraint.annotation;

import dev.noah.word.config.constraint.validator.FileExtensionsConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileExtensionsConstraintValidator.class)
public @interface FileExtensions {

    String message() default "must be a allowed extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {"jpeg", "jpg", "png", "svg", "webp"};
}
