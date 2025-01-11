package com.example.project.emotionCore.config;

import com.example.project.emotionCore.dto.ErrorResponse;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomBadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        //ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 문제가 발생했습니다."); // 실제 배포시에는 이걸로
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
