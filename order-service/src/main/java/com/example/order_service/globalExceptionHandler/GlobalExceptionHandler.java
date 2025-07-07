package com.example.order_service.globalExceptionHandler;

import com.example.order_service.dto.ApiResponseDto;
import com.example.order_service.globalExceptionHandler.customExceptions.InvalidOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ApiResponseDto<ErrorResponse>> handleException(String message, HttpStatus status, List<String> errors){
        return ResponseEntity
                .status(status)
                .body(ApiResponseDto.error(
                        ErrorResponse
                                .builder()
                                .message(message)
                                .status(status.value())
                                .build(),errors
                ));
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleInvalidLoginCredentialsException(InvalidOrderException exception){
        return handleException(exception.getMessage(), HttpStatus.BAD_REQUEST,null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleException(Exception exception){
        return handleException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException exception){
        return handleException(exception.getMessage(), HttpStatus.BAD_REQUEST,null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        List<String> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()+ " : "+ error.getDefaultMessage())
                .collect(Collectors.toList());

        return handleException("Validation error occurred", badRequest,errors);
    }
}
