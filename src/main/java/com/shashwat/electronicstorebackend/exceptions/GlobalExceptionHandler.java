package com.shashwat.electronicstorebackend.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		ResponseMessage message = ResponseMessage.builder()
													.message(ex.getMessage())
													.actionPerformed(true)
													.build();
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
		Map<String, Object> responseForValidationError = new HashMap<>();
		List<ObjectError> errors = ex.getBindingResult().getAllErrors();
		errors.stream().forEach((error) -> {
			String message = error.getDefaultMessage();
			String field = ((FieldError)error).getField();
			responseForValidationError.put(field, message);
		});
		return new ResponseEntity<Map<String,Object>>(responseForValidationError, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseMessage> dataIntegrityViolationException(DataIntegrityViolationException ex){
		ResponseMessage message = ResponseMessage.builder()
													.message(ex.getRootCause().getMessage().toString())
													.actionPerformed(false)
													.build();
		return new ResponseEntity<ResponseMessage>(message,HttpStatus.BAD_REQUEST);
													
	}
	
	@ExceptionHandler(UnsupportedExtensionException.class)
	public ResponseEntity<ResponseMessage> unsupportedExtensionExceptionHandler(UnsupportedExtensionException ex){
		ResponseMessage message = ResponseMessage.builder()
													.message(ex.getMessage())
													.actionPerformed(false)
													.build();
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.BAD_REQUEST);
	}
}
