package com.zainchen.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Configuration
public class MyBatisConfig {
    @Autowired
    private Environment env;

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 动态扫描指定的包路径，并解析出所有需要注册为 MyBatis 类型别名（TypeAlias）的包
     *
     * @param typeAliasesPackage 从 application.yaml 读取的实体包路径
     * @return 处理后的包路径字符串（如 "com.zainchen.common.core.domain"）
     */
    public static String setTypeAliasesPackage(String typeAliasesPackage) {
        // 解析类路径，获取所有类资源路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        List<String> allResult = new ArrayList<>();

        try {
            for (String aliasesPackage : typeAliasesPackage.split(",")) {
                List<String> result = new ArrayList<>();
                // 拼接获取每个指定包路径的类资源路径
                aliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(aliasesPackage.trim()) + "/" + DEFAULT_RESOURCE_PATTERN;
                Resource[] resources = resolver.getResources(aliasesPackage);

                // 读取每个资源的元数据
                if (resources.length > 0) {
                    MetadataReader metadataReader;
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            try {
                                result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                            } catch (ClassNotFoundException e) {
                                log.error("无法加载类: {}", metadataReader.getClassMetadata().getClassName(), e);
                            }
                        }
                    }
                }

                // 去重并合并结果
                if (!result.isEmpty()) {
                    HashSet<String> hashResult = new HashSet<>(result);
                    allResult.addAll(hashResult);
                }
            }

            if (!allResult.isEmpty()) {
                typeAliasesPackage = String.join(",", allResult.toArray(new String[0]));
            } else {
                throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误,参数typeAliasesPackage:" + typeAliasesPackage + "未找到任何包");
            }
        } catch (IOException e) {
            log.error("读取 MyBatis TypeAliases 包路径时发生 IO 异常", e);
        }
        return typeAliasesPackage;
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        if (mapperLocations != null) {
            for (String mapperLocation : mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    log.warn("扫描 mapper 位置 {} 时发生异常: {}", mapperLocation, e.getMessage());
                }
            }
        }
        return resources.toArray(new Resource[0]);
    }

    /**
     * 加载 MyBatis 的 SqlSessionFactory<br>
     * 从 application.yaml 中读取 MyBatis 的相关配置，扫描所有实体类、mapper.xml、mybatis的配置文件
     *
     * @param dataSource 数据源（DruidDataSource）
     * @return SqlSessionFactory 实例
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        String typeAliasesPackage = env.getProperty("mybatis.type-aliases-package");
        String mapperLocations = env.getProperty("mybatis.mapper-locations");
        String configLocation = env.getProperty("mybatis.config-location");

        if (typeAliasesPackage == null || typeAliasesPackage.isEmpty()) {
            throw new RuntimeException("mybatis.typeAliasesPackage 未配置");
        }
        typeAliasesPackage = setTypeAliasesPackage(typeAliasesPackage);
        VFS.addImplClass(SpringBootVFS.class);

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);

        if (mapperLocations == null) {
            log.warn("MyBatis Mapper 文件位置未配置: {}", mapperLocations);
            throw new RuntimeException("mybatis.mapperLocations 未配置");
        }
        sessionFactory.setMapperLocations(resolveMapperLocations(mapperLocations.split(",")));

        if (configLocation == null || configLocation.isEmpty()) {
            log.warn("MyBatis 配置文件不存在: {}", configLocation);
            throw new RuntimeException("mybatis.configLocation 未配置");
        }
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
        return sessionFactory.getObject();
    }
}
