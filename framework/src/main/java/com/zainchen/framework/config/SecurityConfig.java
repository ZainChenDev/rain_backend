package com.zainchen.framework.config;

import com.zainchen.framework.security.filter.JwtAuthenticationTokenFilter;
import com.zainchen.framework.web.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring Security配置
 */
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    /**
     * 自定义用户认证逻辑 {@link UserDetailsServiceImpl}<br>
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 跨域过滤器 {@link ResourcesConfig#corsFilter()}
     */
    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    /**
     * 自定义安全过滤链配置<br>
     * 默认情况下，SecurityFilterChain会将请求重定向到内置login页面（Form认证），在前后端分离的应用中，这种方式不再适用
     *
     * @param http HttpSecurity对象
     * @return SecurityFilterChain对象
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 基于token，不需要CSRF防护
                .csrf(AbstractHttpConfigurer::disable)
                // 基于token，不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headersCustomizer -> headersCustomizer
                        // 禁用浏览器缓存头（Cache-Control）
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                        // 禁用X-Frame-Options，允许iframe嵌套
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                // 添加允许匿名访问的URL
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login", "/captcha").permitAll()
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                // jwt filter，添加在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // cors filter，添加在jwt filter之前
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);

        return http.build();
    }

    /**
     * 明文密码加密器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
