package com.zainchen.common.core.domain;

import lombok.Data;
import com.zainchen.common.enums.Status;

/**
 * 统一返回结果类
 */
@Data
public class CommonResult<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据体
     */
    private T data;

    public static <T> CommonResult<T> ok(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(Status.OK.getCode());
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    /**
     * 使用自定义信息的失败结果
     */
    public static <T> CommonResult<T> failed(int code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 使用默认状态信息的失败结果
     */
    public static <T> CommonResult<T> failed(Status status) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(status.getCode());
        result.setMessage(status.getMessage());
        result.setData(null);
        return result;
    }
}
