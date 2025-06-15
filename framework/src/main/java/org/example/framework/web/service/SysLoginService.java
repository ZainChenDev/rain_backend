package org.example.framework.web.service;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;


    /**
     * 登录验证<br>
     * authenticationManager.authenticate会调用{@link org.example.framework.web.service.UserDetailsServiceImpl}从数据库加载用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 生成的JWT令牌
     */
    public String login(String username, String password, String code, String uuid) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//        AuthenticationContextHolder.setContext(authenticationToken);
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }

    public void loginPreCheck(String username, String password) {
        // 登录前置校验逻辑 isBlank: 校验 null、长度为 0 和纯空白字符
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            // TODO：抛出异常或返回错误信息
        }
    }
}
