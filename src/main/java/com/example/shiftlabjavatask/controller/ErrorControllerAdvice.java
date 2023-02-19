package com.example.shiftlabjavatask.controller;

import com.example.shiftlabjavatask.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductSerialNumberIsBusy.class)
    public ResponseEntity<Map<String, Object>> handleProductSerialNumberIsBusy(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 1, ex);
    }

    @ExceptionHandler(ProductTypeNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductTypeNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 2, ex);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(Exception ex) {
        return handleCustomException(HttpStatus.NOT_FOUND, 3, ex);
    }

    @ExceptionHandler(ProductSerialNumberIsEmpty.class)
    public ResponseEntity<Map<String, Object>> handleProductSerialNumberIsEmpty(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 4, ex);
    }

    @ExceptionHandler(ProductDesktopPcTypeNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductDesktopPcTypeNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 5, ex);
    }

    @ExceptionHandler(ProductLaptopSizeNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductProductLaptopSizeNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 6, ex);
    }

    @ExceptionHandler(ProductScreenDiagonalNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductScreenDiagonalNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 7, ex);
    }

    @ExceptionHandler(ProductHardDriveSizeNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductHardDriveSizeNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 8, ex);
    }

    @ExceptionHandler(ProductDescriptionIsEmpty.class)
    public ResponseEntity<Map<String, Object>> handleProductDescriptionIsEmpty(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 9, ex);
    }

    @ExceptionHandler(ProductPriceNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductPriceNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 10, ex);
    }

    @ExceptionHandler(ProductQuantityNotCorrect.class)
    public ResponseEntity<Map<String, Object>> handleProductQuantityNotCorrect(Exception ex) {
        return handleCustomException(HttpStatus.BAD_REQUEST, 11, ex);
    }

    //Формирует кастомное тело ответа на запрос клиента с кодом и текстом ошибки
    protected Map<String, Object> body(int code, Exception exception) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", exception.getMessage());
        return map;
    }

    protected ResponseEntity<Map<String, Object>> handleCustomException(HttpStatus status,
                                                                        int code, Exception exception) {
        return ResponseEntity.status(status).body(body(code, exception));
    }
}
