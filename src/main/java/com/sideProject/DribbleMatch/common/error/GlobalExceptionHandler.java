package com.sideProject.DribbleMatch.common.error;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected Object handleCustomException(CustomException ex, HttpServletRequest request) {

        if (request.getRequestURI().contains("page")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            return modelAndView;
        } else {
            return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.error(ex.getErrorCode()));
        }
    }

//    @ExceptionHandler(value = ConstraintViolationException.class)
//    protected ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
//        List<String> errorMessages = new ArrayList<>();
//        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            errorMessages.add(violation.getMessage());
//        }
//        return ResponseEntity.status(400)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(ApiResponse.error(ErrorCode.INVALID_DATA_PATTERN, errorMessages.toString()));
//    }
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    protected ResponseEntity<ApiResponse<?>> handleMethodAugumentNotValidException(MethodArgumentNotValidException ex) {
//        List<String> errorMessages = new ArrayList<>();
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
//            errorMessages.add(fieldError.getDefaultMessage());
//        }
//        return ResponseEntity.status(ex.getStatusCode())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(ApiResponse.error(ErrorCode.INVALID_DATA_PATTERN, errorMessages.toString()));
//    }
//
//    @ExceptionHandler(value = HttpMessageConversionException.class)
//    protected ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageConversionException ex) {
//        return ResponseEntity.status(400)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(ApiResponse.error(ErrorCode.INVALID_DATA_PATTERN));
//    }
//
//    @ExceptionHandler(value = MissingRequestHeaderException.class)
//    protected ResponseEntity<ApiResponse<?>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
//        return ResponseEntity.status(ex.getStatusCode())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(ApiResponse.error(ErrorCode.MISSING_REQUEST_HEADER, ex.getHeaderName() + "이/가 비어있습니다."));
//    }

    @ExceptionHandler(value = Exception.class)
    protected Object handleException(Exception ex, HttpServletRequest request) {

        if (request.getRequestURI().contains("page")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            return modelAndView;
        } else {
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.error(ErrorCode.NO_HANDLED_EXCEPTION, ex.getMessage()));
        }
    }
}
