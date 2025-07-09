package com.zainchen.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import com.zainchen.common.core.domain.entity.SysRole;
import com.zainchen.system.mapper.SysRoleMapper;
import com.zainchen.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole role : roles) {
            if (role != null && StringUtils.isNotBlank(role.getRoleKey())) {
                permsSet.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }
}
