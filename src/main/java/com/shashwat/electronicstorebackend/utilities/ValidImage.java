package com.shashwat.electronicstorebackend.utilities;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageVaidator.class)
public @interface ValidImage {

	String message() default "{com.shashwat.electronicstorebackend.utilities.ValidImage.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
}
