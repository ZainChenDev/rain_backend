package org.example.web.controller.system;

import org.example.common.constant.Constants;
import org.example.common.core.domain.CommonResult;
import org.example.common.core.domain.model.LoginBody;
import org.example.framework.web.service.SysLoginService;
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
