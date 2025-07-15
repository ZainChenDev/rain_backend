package com.zainchen.system.service.impl;

import com.zainchen.common.core.domain.entity.SysUser;
import com.zainchen.system.mapper.SysUserMapper;
import com.zainchen.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 根据条件分页查询用户列表
     *
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList() {
        return userMapper.selectUserList();
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }
}
