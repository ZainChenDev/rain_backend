package com.zainchen.web.controller.system;

import com.zainchen.common.core.domain.CommonResult;
import com.zainchen.common.core.domain.entity.SysUser;
import com.zainchen.common.core.domain.model.LoginUser;
import com.zainchen.common.util.SecurityUtils;
import com.zainchen.framework.web.dto.user.UserInfoResponseDTO;
import com.zainchen.framework.web.service.SysPermissionService;
import com.zainchen.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/system/user")
public class SysUserController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @GetMapping("/info")
    public CommonResult<UserInfoResponseDTO> getUserInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        UserInfoResponseDTO userInfo = new UserInfoResponseDTO(user, roles, permissions);
        return CommonResult.ok(userInfo);
    }

//    @GetMapping("/list")
//    public List<SysUser> list() {
//        System.out.println("Fetching user list...");
//        List<SysUser> list = userService.selectUserList();
//        return list;
//    }
}
