package com.property.management.common;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    PHONE_EXISTS(1004, "手机号已存在"),
    PROJECT_NOT_FOUND(2001, "项目不存在"),
    ASSET_NOT_FOUND(3001, "资产不存在"),
    FEE_NOT_FOUND(4001, "费用记录不存在"),
    SERVICE_NOT_FOUND(5001, "服务不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
