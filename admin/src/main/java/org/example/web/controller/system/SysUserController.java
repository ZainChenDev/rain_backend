package org.example.web.controller.system;

import org.example.common.core.domain.entity.SysUser;
import org.example.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class SysUserController {
    @Autowired
    private ISysUserService userService;

//    @GetMapping("/list")
//    public List<SysUser> list() {
//        System.out.println("Fetching user list...");
//        List<SysUser> list = userService.selectUserList();
//        return list;
//    }
}
