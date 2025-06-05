package org.example.framework.web.service;

import org.apache.commons.lang3.StringUtils;

public class SysLoginService {
    public String login(String username, String password, String code, String uuid) {
    }

    public void loginPreCheck(String username, String password) {
        // 登录前置校验逻辑 isBlank: 校验 null、长度为 0 和纯空白字符
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            // TODO：抛出异常或返回错误信息
        }
    }
}
