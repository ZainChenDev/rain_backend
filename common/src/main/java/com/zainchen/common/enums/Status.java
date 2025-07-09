package com.zainchen.common.enums;

import lombok.Getter;

@Getter
public enum Status {
    // 通用状态码
    OK(200, "操作成功"),
    CREATED(201, "创建成功"),

    // 通用错误 (4xx 客户端错误)
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),

    // 通用错误 (5xx 服务器错误)
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),
    UNKNOWN_ERROR(520, "未知错误");


    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
