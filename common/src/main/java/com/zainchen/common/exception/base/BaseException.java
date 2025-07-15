package com.zainchen.common.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块，比如 user、file、job
     */
    private final String module;

    /**
     * 业务错误码，通常结合 enums 定义
     */
    private final Integer code;

    /**
     * 用户可见的提示信息
     */
    private final String message;

    /**
     * 方便内部排查
     */
    private final String detailMessage;
}
