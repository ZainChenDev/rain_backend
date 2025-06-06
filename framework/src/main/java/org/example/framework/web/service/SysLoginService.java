package org.example.framework.web.service;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SysLoginService {
    @Resource
    private AuthenticationManager authenticationManager;

    public void login(String username, String password, String code, String uuid) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//        AuthenticationContextHolder.setContext(authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
    }

    public void loginPreCheck(String username, String password) {
        // 登录前置校验逻辑 isBlank: 校验 null、长度为 0 和纯空白字符
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            // TODO：抛出异常或返回错误信息
        }
    }
}
