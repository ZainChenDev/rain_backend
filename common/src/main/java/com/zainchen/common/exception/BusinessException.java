package com.zainchen.common.exception;

import com.zainchen.common.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务异常类
 * <p>
 * 业务异常通常是指在应用程序的业务逻辑中发生的错误，这些错误可能是由于用户输入不正确、数据不一致、业务规则违反等原因引起的。
 * 业务异常通常需要被捕获并处理，以便向用户提供友好的错误信息或采取其他适当的措施。
 * </p>
 *
 * @author zainchen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务异常码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(Status status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
