package org.example.web.controller.common;

import org.example.common.core.domain.CommonResult;
import org.example.framework.web.dto.login.CaptchaResponseDTO;
import org.example.framework.web.service.CaptchaService;
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
