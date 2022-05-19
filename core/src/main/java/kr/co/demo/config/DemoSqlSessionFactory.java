package kr.co.demo.config;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DemoSqlSessionFactory {
    private static SqlSessionTemplate defaultSqlSession;

    @Inject
    public void setDefaultSqlSession(@Qualifier("defaultSqlSession") SqlSessionTemplate defaultSqlSession) {
        DemoSqlSessionFactory.defaultSqlSession = defaultSqlSession;
    }

    public static SqlSession getSqlSession() {
        return defaultSqlSession;
    }
}
