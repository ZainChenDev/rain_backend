package org.example.framework.web.service;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.common.constant.Constants;
import org.example.common.core.domain.model.LoginUser;
import org.example.common.util.ServletUtils;
import org.example.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expire-time}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

//    public LoginUser getLoginUser(HttpServletRequest request) {
//        String token = getToken(request);
//        if (StringUtils.isNotBlank(token)) {
//            return null;
//        }
//    }

    /**
     * 设置用户身份信息
     *
     * @param loginUser 用户信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        // TODO: 添加ip
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 生成的令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);

        setUserAgent(loginUser);
//        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        return createToken(claims);
    }

//    public void refreshToken(LoginUser loginUser) {
//        loginUser.setLoginTime(System.currentTimeMillis());
//        loginUser.setExpireTime(System.currentTimeMillis() + expireTime * MILLIS_MINUTE);
//
//        String userKey = getLoginUserKey(loginUser.getToken());
//    }

    /**
     * 通过自定义数据声明创建JWT令牌
     *
     * @param claims 自定义数据声明
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims) {
        SecretKey key = Jwts.SIG.HS512.key().build();

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * 鉴权：从Http请求中获取令牌
     *
     * @param request HttpServletRequest对象
     * @return 去除前缀后的令牌字符串
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotBlank(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }
}
