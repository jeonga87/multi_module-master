package kr.co.demo.config.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DemoProperties implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;
    private Logger logger = LoggerFactory.getLogger(DemoProperties.class);

    public DemoProperties() {
        logger.info("init DemoProperties");
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }

    public static Environment getEnvironment() {
        return CONTEXT.getEnvironment();
    }
}