package com.zainchen.system.service;

import com.zainchen.common.core.domain.entity.SysUser;

public interface ISysUserService {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);
}
