package com.andrew.wiki.controller;


import com.andrew.wiki.exception.BusinessException;
import com.andrew.wiki.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Unified exception handling, data preprocessing, etc.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Unified processing of verification exceptions
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BindException e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.warn("Parameter verification failed：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResponse.setSuccess(false);
        commonResponse.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResponse;
    }


    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BusinessException e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.warn("Business exception：{}", e.getCode().getDesc());
        commonResponse.setSuccess(false);
        commonResponse.setMessage(e.getCode().getDesc());
        return commonResponse;
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(Exception e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.error("System Exception：", e);
        commonResponse.setSuccess(false);
        commonResponse.setMessage("The system has exception, please contact the administrator");
        return commonResponse;
    }
}
