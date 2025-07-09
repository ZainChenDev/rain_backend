package com.zainchen.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zainchen.**.mapper")
public class ApplicationConfig {

}
