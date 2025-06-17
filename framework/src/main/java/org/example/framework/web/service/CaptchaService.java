package org.example.framework.web.service;

import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.common.config.RainConfig;
import org.example.common.utils.uuid.IdUtils;
import org.example.framework.web.dto.CaptchaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class CaptchaService {
    // 验证码类型：数学表达式
    private static final String CAPTCHA_TYPE_MATH = "math";
    // 验证码类型：字符
    private static final String CAPTCHA_TYPE_CHAR = "char";

    // 数学表达式结果分隔符
    private static final String MATH_SEPARATOR = "@";

    // 验证码图像格式
    private static final String IMAGE_FORMAT = "jpg";

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    private final RainConfig rainConfig;

    @Autowired
    public CaptchaService(RainConfig rainConfig) {
        this.rainConfig = rainConfig;
    }

    public CaptchaResponseDTO generateCaptcha() {
        // TODO: 验证码开关逻辑
        boolean captchaEnabled = true;

        CaptchaResponseDTO captchaResponseDTO = new CaptchaResponseDTO();
        captchaResponseDTO.setCaptchaEnabled(captchaEnabled);

        if (!captchaEnabled) {
            return captchaResponseDTO;
        }

        try {
            CaptchaData captchaData = createCaptcha();

            // 生成UUID并保存到缓存
            String uuid = IdUtils.simpleUUID();
            // TODO: 将验证码数据保存到缓存中，使用uuid作为key
            String base64Image = convertImageToBase64(captchaData.getImage());

            captchaResponseDTO.setUuid(uuid);
            captchaResponseDTO.setImg(base64Image);

            return captchaResponseDTO;
        } catch (Exception e) {
            log.error("生成验证码失败", e);
            throw new RuntimeException("验证码生成失败", e);
        }

    }

    /**
     * 生成验证码
     */
    private CaptchaData createCaptcha() {
        String captchaType = rainConfig.getCaptchaType();

        switch (captchaType) {
            case CAPTCHA_TYPE_MATH:
                return generateMathCaptcha();
            case CAPTCHA_TYPE_CHAR:
                return generateCharCaptcha();
            default:
                log.warn("未知的验证码类型: {}, 使用默认字符验证码", captchaType);
                return generateCharCaptcha();
        }
    }

    /**
     * 生成数学表达式类型验证码
     */
    private CaptchaData generateMathCaptcha() {
        // example: capText = "1+2=@3"
        String capText = captchaProducerMath.createText();
        // 检查数学验证码分隔符
        int separatorIndex = capText.lastIndexOf(MATH_SEPARATOR);
        if (separatorIndex == -1) {
            throw new IllegalStateException("数学验证码格式错误，缺少分隔符: " + capText);
        }

        String capStr = capText.substring(0, separatorIndex);
        String code = capText.substring(separatorIndex + 1);
        BufferedImage image = captchaProducerMath.createImage(capStr);
        return new CaptchaData(capStr, code, image);
    }

    /**
     * 生成字符类型验证码
     */
    private CaptchaData generateCharCaptcha() {
        String capStr = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(capStr);
        return new CaptchaData(capStr, capStr, image);
    }

    /**
     * 将图像转换为Base64字符串
     */
    private String convertImageToBase64(BufferedImage image) throws IOException {
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            ImageIO.write(image, IMAGE_FORMAT, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
    }

    /**
     * 验证码封装类
     */
    @Getter
    @AllArgsConstructor
    private static class CaptchaData {
        /**
         * 验证码显示文本
         */
        private final String displayText;

        /**
         * 验证码结果
         */
        private final String result;

        /**
         * 验证码图像
         */
        private final BufferedImage image;
    }
}
