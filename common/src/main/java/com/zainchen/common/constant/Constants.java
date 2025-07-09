package com.zainchen.common.constant;

import io.jsonwebtoken.Claims;

public class Constants {
    /**
     * jwt令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * jwt用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.zainchen" };
}
