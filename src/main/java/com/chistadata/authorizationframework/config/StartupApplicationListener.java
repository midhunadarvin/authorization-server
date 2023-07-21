package com.chistadata.authorizationframework.config;

import com.chistadata.authorizationframework.utils.DatabaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private DatabaseConfiguration configuration;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        DatabaseHelper.init(
                configuration.getHostname(),
                configuration.getPort(),
                configuration.getUsername(),
                configuration.getPassword()
        );
    }
}
