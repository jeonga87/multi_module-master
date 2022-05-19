package kr.co.demo.util;

import kr.co.demo.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * 현재 Application의 이름을 가져온다 [admin|pc-web|mobile-web|batch]
     * @return
     */
    public static String getAppName() {
        String appName = null;

        String rawAppName = env.getProperty("spring.application.name");
        if(StringUtils.isNotEmpty(rawAppName)) {
            appName = rawAppName.substring("demo-".length());
        }

        return appName;
    }

    /**
     * 현재 Application이 운영환경(prod)인지 여부
     * @return
     */
    public static boolean isProd() {
        return Constants.SpringProfileConst.PRODUCTION.equals(env.getProperty("spring.profiles.active"));
    }

    /**
     * 현재 Application이 ADMIN인지 여부
     * @return
     */
    public static boolean isAdmin() {
        return "admin".equals(getAppName());
    }

    /**
     * 현재 Application이 PC-WEB인지 여부
     * @return
     */
    public static boolean isPcWeb() {
        return "pc-web".equals(getAppName());
    }

    /**
     * 현재 Application이 MOBILE-WEB인지 여부
     * @return
     */
    public static boolean isMobileWeb() {
        return "mobile-web".equals(getAppName());
    }

    /**
     * 현재 Application의 URL을 가져온다. (batch는 안가져옴)
     * @return
     */
    public static String getUrl() {
        String appName = getAppName();
        if(appName != null) {
            return env.getProperty("demo.url." + appName);
        } else {
            return null;
        }
    }
}