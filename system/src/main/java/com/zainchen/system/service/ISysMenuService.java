package com.zainchen.system.service;

import com.zainchen.common.core.domain.entity.SysMenu;

import java.util.List;
import java.util.Set;

public interface ISysMenuService {
    /**
     * 根据用户查询系统菜单列表
     *
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList();

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);
}
