package com.example.auth_service.dto;

import com.example.auth_service.globalExceptionHandler.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {
    private T data;
    private int status;
    private boolean success;
    private List<String> error;
    private String message;
    private Date timestamp;

    public static <T> ApiResponseDto<T> success(T data, int status){
        return ApiResponseDto.<T>builder()
                .data(data)
                .status(status)
                .success(true)
                .timestamp(new Date())
                .build();
    }

    public static <T> ApiResponseDto<T> error(ErrorResponse data, List<String> error){
        return ApiResponseDto.<T>builder()
                .error(error)
                .message(data.message())
                .status(data.status())
                .timestamp(new Date())
                .build();
    }
}
