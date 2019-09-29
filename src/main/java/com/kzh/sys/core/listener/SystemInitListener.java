package com.kzh.sys.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemInitListener extends Thread implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(SystemInitListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {

    }


    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {

    }
}
