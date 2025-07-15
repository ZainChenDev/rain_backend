package com.zainchen.common.util.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * SQL工具类
 */
@Slf4j
public class SqlUtils {
    /**
     * OrderBy最大长度限制
     */
    private final static int orderByMaxLength = 500;

    /**
     * 允许的OrderBy字符模式
     */
    private static final Pattern VALID_ORDER_BY_PATTERN = Pattern.compile(
            "^[a-zA-Z][a-zA-Z0-9_]*(?:\\s*,\\s*[a-zA-Z][a-zA-Z0-9_]*)*(?:\\s+(asc|desc|ASC|DESC))?$"
    );

    /**
     * 转义OrderBy SQL参数
     *
     * @param value 待验证的OrderBy字符串
     * @return 验证通过的OrderBy字符串
     * @throws SecurityException 如果验证失败
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }

        // 长度检查
        if (value.length() > orderByMaxLength) {
            log.warn("OrderBy参数长度超限: length={}, max={}", value.length(), orderByMaxLength);
            throw new SecurityException(
                    String.format("OrderBy参数长度超限，当前长度: %d, 最大允许: %d", value.length(), orderByMaxLength)
            );
        }
        return value;
    }

    private static void validateOrderBy(String value) {
        String normalizedValue = normalizeOrderBy(value);

        // 1. 基础格式验证
        if (!VALID_ORDER_BY_PATTERN.matcher(normalizedValue).matches()) {
            throw new SecurityException("OrderBy参数格式不正确: " + value);
        }
    }

    /**
     * 标准化OrderBy字符串，去除多余空格和小写化
     */
    private static String normalizeOrderBy(String value) {
        return value.trim()
                .replaceAll("\\s+", " ")  // 多个空格替换为单个
                .toLowerCase();
    }
}
