package org.example.common.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.constant.UserConstants;
import org.example.common.core.domain.BaseEntity;

import java.io.Serial;
import java.util.Set;

/**
 * 角色表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    private Long[] deptIds;

    /**
     * 角色权限
     */
    private Set<String> permissions;

    /**
     * 判断当前用户是否为管理员
     */
    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    /**
     * 判断当前用户是否为管理员 static方法
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ROLE_ID.equals(userId);
    }
}
