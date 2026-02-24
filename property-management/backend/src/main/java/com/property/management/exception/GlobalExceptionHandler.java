package com.property.management.exception;

import com.property.management.common.Result;
import com.property.management.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 - 请求路径: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("参数异常 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "参数错误：" + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "参数校验失败：" + message);
    }

    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "参数绑定失败：" + message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("约束校验失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "参数校验失败：" + message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("缺少请求参数 - 请求路径: {}, 参数名: {}", request.getRequestURI(), e.getParameterName());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "缺少必填参数：" + e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("请求体解析失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "请求体格式错误，请检查JSON格式");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("参数类型转换失败 - 请求路径: {}, 参数名: {}, 期望类型: {}", 
                request.getRequestURI(), e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "参数类型错误：" + e.getName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("请求方法不支持 - 请求路径: {}, 请求方法: {}, 支持的方法: {}", 
                request.getRequestURI(), e.getMethod(), String.join(", ", e.getSupportedMethods()));
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED.value(), "不支持的请求方法：" + e.getMethod());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<String> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("接口不存在 - 请求路径: {}, 请求方法: {}", request.getRequestURI(), e.getHttpMethod());
        return Result.error(ResultCode.NOT_FOUND.getCode(), "接口不存在：" + request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result<String> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("认证失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<String> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("身份认证失败 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.UNAUTHORIZED.getCode(), "身份认证失败：" + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("访问被拒绝 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.FORBIDDEN.getCode(), "没有权限访问该资源");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常 - 请求路径: {}, 异常类型: {}, 错误信息: {}", 
                request.getRequestURI(), e.getClass().getName(), e.getMessage(), e);
        return Result.error("系统繁忙，请稍后重试");
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 - 请求路径: {}, 异常类型: {}, 错误信息: {}", 
                request.getRequestURI(), e.getClass().getName(), e.getMessage(), e);
        return Result.error("系统异常，请联系管理员");
    }
}
