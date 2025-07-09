package org.example.framework.config;

import org.example.framework.security.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring Security配置
 */
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    /**
     * 自定义用户认证逻辑 {@link org.example.framework.web.service.UserDetailsServiceImpl}<br>
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 跨域过滤器 {@link org.example.framework.config.ResourcesConfig#corsFilter()}
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
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 添加允许匿名访问的URL
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login", "/captcha").permitAll()
                        .anyRequest().authenticated()
                )
                // 进行jwt验证
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
