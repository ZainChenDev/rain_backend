package org.example.web.controller.system;

import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLoginController {
    @Resource
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String hello(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 调用认证方法
            // 1.如果认证成功，返回 Authentication 对象，其中封装了用户的全部信息
            // 2.如果认证失败，返回 null
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication == null) {
                // 认证失败逻辑
                return "Authentication failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
