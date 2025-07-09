package com.zainchen.framework.web.service;

import com.zainchen.common.core.domain.entity.SysUser;
import com.zainchen.common.core.domain.model.LoginUser;
import com.zainchen.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 从数据库加载用户信息的服务实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        return new LoginUser(user.getUserId(), user.getDeptId(), user);
    }
}
