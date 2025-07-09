package com.zainchen.framework.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.zainchen.common.core.domain.entity.SysUser;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserInfoResponseDTO {
    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;
}
