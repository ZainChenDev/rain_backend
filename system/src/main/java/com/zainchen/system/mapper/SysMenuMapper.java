package com.zainchen.system.mapper;

import com.zainchen.common.core.domain.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper {
    List<SysMenu> selectMenuList();
    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);
}
