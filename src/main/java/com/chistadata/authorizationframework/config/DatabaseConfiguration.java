package com.chistadata.authorizationframework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("database")
@Getter
@Setter
public class DatabaseConfiguration {
    private String hostname;
    private String port;
    private String username;
    private String password;
}
