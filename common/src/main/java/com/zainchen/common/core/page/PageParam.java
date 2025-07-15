package com.zainchen.common.core.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页查询参数
 * 用于封装分页请求的参数，包括页码、每页显示数量、排序字段和方向等。
 * 支持合理化分页和标准化排序字符串的获取。
 */
@Data
public class PageParam {
    /**
     * 当前页码
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页显示数量
     */
    @Min(value = 1, message = "每页显示数量必须大于0")
    @Max(value = 100, message = "每页显示数量不能超过100")
    private Integer pageSize = 10;

    /**
     * 排序字段s
     * 只能包含字母、数字和下划线，且必须以字母或下划线开头
     */
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "排序字段格式不正确")
    private String orderByColumn;

    /**
     * 排序方向
     * 只能是 "asc" 或 "desc"，不区分大小写
     */
    @Pattern(regexp = "^(asc|desc|ascending|descending)$", message = "排序方向只能是asc或desc")
    private String isAsc = "asc";

    /**
     * 是否合理化分页
     * 当该参数设置为 true 时，pageNum<=0 时会查询第一页， pageNum>pages（超过总数时），会查询最后一页。默认false 时，直接根据参数进行查询。
     */
    private Boolean reasonable = true;

    /**
     * 获取标准化的排序字符串，排序字段和方向
     */
    public String getOrderByColumn() {
        if (StringUtils.isBlank(orderByColumn)) {
            return "";
        }

        String direction = normalizeDirection(isAsc);
        String column = camelToUnderline(orderByColumn);

        return column + " " + direction;
    }

    /**
     * 标准化排序方向，将 "ascending" 和 "descending" 转换为 "asc" 和 "desc"
     */
    private String normalizeDirection(String direction) {
        if ("ascending".equalsIgnoreCase(direction)) {
            return "asc";
        } else if ("descending".equalsIgnoreCase(direction)) {
            return "desc";
        }
        return direction.toLowerCase();
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    private static String camelToUnderline(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            boolean curIsUpperCase = Character.isUpperCase(c);
            boolean preIsUpperCase = i > 0 && Character.isUpperCase(str.charAt(i - 1));
            boolean nextIsUpperCase = i < len - 1 && Character.isUpperCase(str.charAt(i + 1));

            if ((preIsUpperCase && curIsUpperCase && !nextIsUpperCase) ||
                    (!preIsUpperCase && curIsUpperCase)) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
