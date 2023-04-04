package com.shashwat.electronicstorebackend.utilities;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageVaidator implements ConstraintValidator<ValidImage, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if (value.isBlank()) {
			return false;
		}
		else {
			return true;
		}
	}

}
