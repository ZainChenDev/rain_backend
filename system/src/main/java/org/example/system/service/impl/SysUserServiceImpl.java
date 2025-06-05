package org.example.system.service.impl;

import org.example.common.core.domain.entity.SysUser;
import org.example.system.mapper.SysUserMapper;
import org.example.system.service.ISysUserService;
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
}
