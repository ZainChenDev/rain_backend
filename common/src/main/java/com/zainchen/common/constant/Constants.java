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
}
