package org.example.system.mapper;

import org.example.common.core.domain.entity.SysUser;

import java.util.List;

public interface SysUserMapper {
    /**
     * 根据条件分页查询用户列表
     *
     * @return 用户列表
     */
    List<SysUser> selectUserList();

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);
}
