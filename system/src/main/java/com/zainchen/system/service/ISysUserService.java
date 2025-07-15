package com.zainchen.system.service;

import com.zainchen.common.core.domain.entity.SysUser;

import java.util.List;

public interface ISysUserService {

    /**
     * 根据条件分页查询用户列表
     *
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList();

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);
}
