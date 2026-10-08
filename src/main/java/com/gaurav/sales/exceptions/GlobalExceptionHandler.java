package com.gaurav.sales.exceptions;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// RESOURCE NOT FOUND
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(false).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	// DUPLICATE RESOURCE
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(false).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	// BAD REQUEST
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(false).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// VALIDATION ERROR
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
			MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder().success(false)
				.message("Validation failed").data(errors).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// CONSTRAINT VIOLATION
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(false).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// ALL OTHER EXCEPTIONS
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(false).message("Something went wrong")
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
