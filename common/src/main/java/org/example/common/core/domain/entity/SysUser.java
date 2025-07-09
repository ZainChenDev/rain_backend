package org.example.common.core.domain.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.constant.UserConstants;
import org.example.common.core.domain.BaseEntity;

/**
 * 用户信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 密码最后更新时间
     */
    private LocalDateTime pwdUpdateTime;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 判断当前用户是否为管理员
     */
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    /**
     * 判断当前用户是否为管理员 static方法
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_USER_ID.equals(userId);
    }
}