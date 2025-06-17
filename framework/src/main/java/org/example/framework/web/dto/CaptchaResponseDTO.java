package org.example.framework.web.dto;

import lombok.Data;

/**
 * 验证码响应数据传输对象
 */
@Data
public class CaptchaResponseDTO {
    /**
     * 验证码是否启用
     */
    private Boolean captchaEnabled;

    /**
     * 验证码唯一标识
     */
    private String uuid;

    /**
     * 验证码图片的Base64字符串
     */
    private String img;
}
