package com.abouna.sante.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class ApplicationContextFactory {
    private static final ApplicationContext applicationContext = buildApplicationContextFactory();

    private static ApplicationContext buildApplicationContextFactory() {
        try {
            return new ClassPathXmlApplicationContext("config/spring-config.xml");
        }
        catch (Throwable ex) {
// Make sure you log the exception, as it might be swallowed
            System.err.println("Initial Application Context creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
