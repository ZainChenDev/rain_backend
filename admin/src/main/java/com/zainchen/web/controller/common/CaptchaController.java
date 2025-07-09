package com.zainchen.web.controller.common;

import com.zainchen.common.core.domain.CommonResult;
import com.zainchen.framework.web.dto.login.CaptchaResponseDTO;
import com.zainchen.framework.web.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码
     *
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public CommonResult<CaptchaResponseDTO> getCaptcha() {
        CaptchaResponseDTO captchaResponse = captchaService.generateCaptcha();
        return CommonResult.ok(captchaResponse);
    }
}
