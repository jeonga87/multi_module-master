package kr.co.demo.config;

import kr.co.demo.config.mybatis.interceptor.CurrentInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;

/**
 *  dataSource를 참조 하여 db 와 mybatis 연결 설정
 * */
public abstract class MyBatisConfiguration {

    public static final String TYPE_ALIASES_PACKAGE = "kr.co.demo";
    public static final String TYPE_HANDLER_PACKAGE = "kr.co.demo.config.mybatis.type";
    public static final String MAPPER_LOCATIONS_PATH = "classpath:mybatis/sql/*.xml";

    protected void configureSqlSessionFactory(DataSource dataSource, SqlSessionFactoryBean sessionFactoryBean) throws IOException {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); //컬럼명에 언더바가 있을경우 카멜케이스로 읽는다
        configuration.setJdbcTypeForNull(JdbcType.NULL); //mybatis 에서 null 값 입력 시 오류발생 처리

        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE); // @Alias 읽기 세팅
        sessionFactoryBean.setTypeHandlersPackage(TYPE_HANDLER_PACKAGE);
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
        sessionFactoryBean.setPlugins(new Interceptor[]{new CurrentInterceptor()});
        sessionFactoryBean.setConfiguration(configuration);
        sessionFactoryBean.setVfs(SpringBootVFS.class);
    }

}

@Configuration
@MapperScan(basePackages = "kr.co.demo.**.repository", annotationClass = Repository.class, sqlSessionFactoryRef = "defaultSqlSessionFactory")
class DefaultMyBatisConfiguration extends MyBatisConfiguration {

    /**
     *  SqlSessionTemplate : SqlSession을 구현하고 코드에서 SqlSession를 대체하는 역할
     *                       쓰레드에 안전하고 여러개의 DAO 나 매퍼에서 공유할수 있다.
     * */
    @Primary
    @Bean
    public SqlSessionTemplate defaultSqlSession(@Qualifier("defaultSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    /**
     * SqlSessionFactoryBean : 데이터베이스와의 연결과 SQL의 실행에 대한 모든 것을 가진 객체
     * */
    @Primary
    @Bean
    public SqlSessionFactory defaultSqlSessionFactory(@Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(dataSource, sessionFactoryBean);

        return sessionFactoryBean.getObject();
    }
}
