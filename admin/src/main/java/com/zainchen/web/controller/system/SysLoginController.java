package com.zainchen.web.controller.system;

import com.zainchen.common.constant.Constants;
import com.zainchen.common.core.domain.CommonResult;
import com.zainchen.common.core.domain.model.LoginBody;
import com.zainchen.framework.web.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @PostMapping("/login")
    public CommonResult<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(),
                loginBody.getCode(), loginBody.getUuid());

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.TOKEN, token);
        return CommonResult.ok(data);
    }
}
