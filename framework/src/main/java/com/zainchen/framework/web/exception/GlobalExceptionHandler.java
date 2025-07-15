package com.zainchen.framework.web.exception;

import com.zainchen.common.core.domain.CommonResult;
import com.zainchen.common.enums.Status;
import com.zainchen.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获所有自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBaseException(BusinessException e) {
        log.error("业务异常 {}", e.getMessage(), e);
        return CommonResult.failed(e.getCode(), e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return CommonResult.failed(Status.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return CommonResult.failed(Status.UNKNOWN_ERROR.getCode(), "未知错误");
    }
}
