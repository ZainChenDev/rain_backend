package com.zainchen.web.controller.system;

import com.zainchen.common.core.controller.BaseController;
import com.zainchen.common.core.domain.CommonResult;
import com.zainchen.common.core.domain.entity.SysMenu;
import com.zainchen.common.core.page.PageParam;
import com.zainchen.common.core.page.TableDataInfo;
import com.zainchen.system.service.ISysMenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    @GetMapping("/list")
    public CommonResult<TableDataInfo> list(@Valid PageParam pageParam) {
        startPage(pageParam);
        List<SysMenu> list = menuService.selectMenuList();
        return CommonResult.ok(getDataTable(list));
    }
}
