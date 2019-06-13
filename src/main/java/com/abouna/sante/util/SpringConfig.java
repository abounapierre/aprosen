package com.abouna.sante.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
@Component
//@ComponentScan(basePackages = {"com.abouna.sante.dao.impl", "com.abouna.sante.service.impl"})
@ImportResource("classpath:/config/spring-config.xml")
public class SpringConfig {
    @Bean
    public ReloadableResourceBundleMessageSource getMessages() {
        ReloadableResourceBundleMessageSource msg = new ReloadableResourceBundleMessageSource();
        msg.setBasename("classpath:messages");
        return msg;
    }
    
}
