package com.zainchen.framework.web.service;

import com.zainchen.common.constant.CacheConstants;
import com.zainchen.common.core.redis.RedisClient;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.zainchen.common.constant.Constants;
import com.zainchen.common.core.domain.model.LoginUser;
import com.zainchen.common.util.ServletUtils;
import com.zainchen.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class TokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥 TODO 使用更好的方式存储秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expire-time}")
    private int expireTime;

    @Autowired
    private RedisClient redisClient;

    // JWT签名算法，使用HS256
    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS512;

    // 秒转换为毫秒
    protected static final long MILLIS_SECOND = 1000;

    // 分钟转换为毫秒
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    // 令牌刷新时间阈值，20分钟
    private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

    /**
     * 从Http请求中获取令牌，由令牌中解析用户uuid，再从redis中获取登录信息
     *
     * @param request HttpServletRequest对象
     * @return 登录信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotBlank(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及登录信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            return (LoginUser) redisClient.get(userKey);
            //TODO 处理异常
        }
        return null;
    }

    /**
     * 设置用户身份信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        // TODO: 添加ip
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 创建令牌，包括uid、用户名<br>
     * 设置用户代理信息，刷新令牌有效期（缓存到redis）<br>
     *
     * @param loginUser 登录信息
     * @return 生成的令牌
     */
    public String createToken(LoginUser loginUser) {
        String uuid = IdUtils.fastUUID();
        loginUser.setToken(uuid);

        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, uuid);
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，如果相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期，保存到redis中，key为带标识的uuid，value为登录用户信息<br>
     * redis缓存的过期时间expireTime，单位为分钟TimeUnit.MINUTES
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(System.currentTimeMillis() + expireTime * MILLIS_MINUTE);

        String userKey = getTokenKey(loginUser.getToken());
        redisClient.set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 通过自定义数据声明创建JWT令牌
     *
     * @param claims 自定义数据声明
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)
                .signWith(key, ALGORITHM)
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

    /**
     * 解析令牌，获取Claims对象
     *
     * @param token 加密的JWT令牌
     * @return Claims对象，包含令牌中的自定义数据
     */
    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 将uuid拼装为登录用户的缓存key
     *
     * @param uuid 登录用户的唯一标识
     * @return 登录用户的缓存key
     */
    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
