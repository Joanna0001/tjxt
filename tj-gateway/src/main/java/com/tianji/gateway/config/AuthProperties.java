package com.tianji.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "tj.auth")
public class AuthProperties implements InitializingBean {

    private Set<String> excludePath = new HashSet<>();   // ① 关键：立即初始化

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加默认不拦截的路径
        excludePath.add("POST:/error/**");
        excludePath.add("GET:/jwks");
        excludePath.add("POST:/as/accounts/login");
        excludePath.add("POST:/as/accounts/admin/login");
        excludePath.add("GET:/as/accounts/refresh");
    }
}
