package com.zainchen.common.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 表格数据对象
 * 用于封装表格数据的总数和行数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDataInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;
}