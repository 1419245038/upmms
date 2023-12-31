package com.gd.upmms.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        String message = exception.getMessage();
        log.info(message);
        if (message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            return R.error(split[2]+"已存在!");
        }

        return R.error("未知错误!");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        String message = exception.getMessage();
        log.info(message);
        return R.error(message);
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception exception){
        String message = exception.getMessage();
        log.info(message);
        return R.error(message);
    }
}
