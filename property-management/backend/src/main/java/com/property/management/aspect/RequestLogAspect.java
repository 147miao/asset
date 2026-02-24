package com.property.management.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.property.management.controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        String requestId = generateRequestId();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = getClientIp(request);
        
        logRequest(requestId, method, uri, ip, point, request);
        
        Object result = null;
        Throwable exception = null;
        
        try {
            result = point.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logResponse(requestId, method, uri, duration, result, exception);
        }
    }

    private void logRequest(String requestId, String method, String uri, String ip, 
                           ProceedingJoinPoint point, HttpServletRequest request) {
        try {
            String className = point.getTarget().getClass().getSimpleName();
            String methodName = point.getSignature().getName();
            
            Map<String, Object> requestInfo = new HashMap<>();
            requestInfo.put("requestId", requestId);
            requestInfo.put("method", method);
            requestInfo.put("uri", uri);
            requestInfo.put("ip", ip);
            requestInfo.put("controller", className + "." + methodName);
            
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if ("authorization".equalsIgnoreCase(headerName) || "token".equalsIgnoreCase(headerName)) {
                    headers.put(headerName, "******");
                } else {
                    headers.put(headerName, request.getHeader(headerName));
                }
            }
            requestInfo.put("headers", headers);
            
            Object[] args = point.getArgs();
            if (args != null && args.length > 0) {
                Map<String, Object> params = new HashMap<>();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof HttpServletRequest || 
                        arg instanceof HttpServletResponse || 
                        arg instanceof MultipartFile) {
                        continue;
                    }
                    try {
                        params.put("arg" + i, objectMapper.valueToTree(arg));
                    } catch (Exception e) {
                        params.put("arg" + i, arg != null ? arg.toString() : "null");
                    }
                }
                if (!params.isEmpty()) {
                    requestInfo.put("params", params);
                }
            }
            
            log.info("[{}] 请求开始 - {}", requestId, objectMapper.writeValueAsString(requestInfo));
        } catch (Exception e) {
            log.warn("[{}] 记录请求日志失败: {}", requestId, e.getMessage());
        }
    }

    private void logResponse(String requestId, String method, String uri, long duration, 
                            Object result, Throwable exception) {
        try {
            Map<String, Object> responseInfo = new HashMap<>();
            responseInfo.put("requestId", requestId);
            responseInfo.put("method", method);
            responseInfo.put("uri", uri);
            responseInfo.put("duration", duration + "ms");
            
            if (exception != null) {
                responseInfo.put("status", "error");
                responseInfo.put("error", exception.getClass().getSimpleName());
                responseInfo.put("message", exception.getMessage());
                log.error("[{}] 请求异常 - {}", requestId, objectMapper.writeValueAsString(responseInfo));
            } else {
                responseInfo.put("status", "success");
                if (result != null) {
                    try {
                        String resultStr = objectMapper.writeValueAsString(result);
                        if (resultStr.length() > 500) {
                            resultStr = resultStr.substring(0, 500) + "...(truncated)";
                        }
                        responseInfo.put("result", resultStr);
                    } catch (Exception e) {
                        responseInfo.put("result", "无法序列化结果");
                    }
                }
                log.info("[{}] 请求完成 - {}", requestId, objectMapper.writeValueAsString(responseInfo));
            }
        } catch (Exception e) {
            log.warn("[{}] 记录响应日志失败: {}", requestId, e.getMessage());
        }
    }

    private String generateRequestId() {
        return String.valueOf(System.currentTimeMillis()) + "-" + (int)(Math.random() * 10000);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
