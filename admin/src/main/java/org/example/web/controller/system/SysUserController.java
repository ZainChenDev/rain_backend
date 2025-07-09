package org.example.web.controller.system;

import org.example.common.core.domain.CommonResult;
import org.example.common.core.domain.entity.SysUser;
import org.example.common.core.domain.model.LoginUser;
import org.example.common.util.SecurityUtils;
import org.example.framework.web.dto.user.UserInfoResponseDTO;
import org.example.framework.web.service.SysPermissionService;
import org.example.system.service.ISysUserService;
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
