package org.example.system.service;

import org.example.common.core.domain.entity.SysUser;

import java.util.List;

public interface ISysUserService {
    List<SysUser> selectUserList();
}
