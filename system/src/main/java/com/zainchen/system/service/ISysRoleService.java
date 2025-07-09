package com.zainchen.system.service;

import java.util.Set;

public interface ISysRoleService {

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolePermissionByUserId(Long userId);
}
