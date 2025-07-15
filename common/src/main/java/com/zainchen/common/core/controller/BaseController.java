package com.zainchen.common.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zainchen.common.core.page.PageParam;
import com.zainchen.common.core.page.TableDataInfo;
import com.zainchen.common.util.sql.SqlUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * web层通用数据处理
 */
@Slf4j
public abstract class BaseController {
    /**
     * 设置请求分页数据
     */
    protected void startPage(PageParam pageParam) {
        // 如果 pageParam 为空，则创建一个默认 PageParam 实例
        if (pageParam == null) {
            pageParam = new PageParam();
        }

        String orderBy = SqlUtils.escapeOrderBySql(pageParam.getOrderByColumn());
        PageHelper.startPage(
                pageParam.getPageNum(),
                pageParam.getPageSize(),
                orderBy
        ).setReasonable(pageParam.getReasonable());
    }

    /**
     * 将数据列表封装为 TableDataInfo 对象
     *
     * @param list 数据列表
     * @return TableDataInfo
     */
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(list);

        if (list instanceof Page<?> page) {
            rspData.setTotal(page.getTotal());
        } else {
            rspData.setTotal(new PageInfo<>(list).getTotal());
        }

        return rspData;
    }
}
