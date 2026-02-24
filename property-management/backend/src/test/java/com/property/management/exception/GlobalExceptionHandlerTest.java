package com.property.management.exception;

import com.property.management.common.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
    }

    @Test
    void testHandleBusinessException() {
        BusinessException ex = new BusinessException(400, "业务异常");
        Result<String> result = handler.handleBusinessException(ex, request);

        assertNotNull(result);
        assertEquals(400, result.getCode());
        assertEquals("业务异常", result.getMessage());
    }

    @Test
    void testHandleBusinessException_DefaultCode() {
        BusinessException ex = new BusinessException("业务异常");
        Result<String> result = handler.handleBusinessException(ex, request);

        assertNotNull(result);
        assertEquals(500, result.getCode());
    }

    @Test
    void testHandleBindException() {
        BindException ex = new BindException(new Object(), "object");
        ex.addError(new FieldError("object", "field", "错误信息"));

        Result<String> result = handler.handleBindException(ex, request);

        assertNotNull(result);
        assertNotEquals(200, result.getCode());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("参数错误");
        Result<String> result = handler.handleIllegalArgumentException(ex, request);

        assertNotNull(result);
        assertNotEquals(200, result.getCode());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("运行时异常");
        Result<String> result = handler.handleRuntimeException(ex, request);

        assertNotNull(result);
        assertNotEquals(200, result.getCode());
    }

    @Test
    void testHandleException() {
        Exception ex = new Exception("未知异常");
        Result<String> result = handler.handleException(ex, request);

        assertNotNull(result);
        assertNotEquals(200, result.getCode());
    }
}
