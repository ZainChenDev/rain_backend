package com.zainchen.system.mapper;

import com.zainchen.common.core.domain.entity.SysRole;

import java.util.List;

public interface SysRoleMapper {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);
}
