package kr.co.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

public abstract class DatabaseConfiguration{

}

/**
 * 데이터소스, 트랜젝션 설정
 * Connection Pool : 데이터베이스와 연결된 커넥션을 미리 만들어 pool 속에 저장해 두고 있다가 필요할 때 커넥션 풀을 쓰고 다시 반환하는 기법
 * spring 2.0 version 이전에는 tomcat jdbc Connection Pool 을 사용 했으나, 그 이후 HikariCP 로 default 가 변경되었다.
 * 빠르고, 단순하고, 안정적, 가벼움
 */
@Configuration
@EnableTransactionManagement
@Setter
@Getter
@ConfigurationProperties(prefix = "demo.datasource")
class DefaultDatabaseConfiguration extends DatabaseConfiguration {

    private String url;
    private String type;
    private String driverClassName;
    private String username;
    private String password;

    @Primary // 같은 우선순위로 있는 클래스가 여러개가 있을 시 그 중 가장 우선순위로 주입할 클래스 타입을 선택
    @Bean(name = "defaultDataSource")
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);

        //sql 출력 format
        Log4jdbcProxyDataSource logDataSource = new Log4jdbcProxyDataSource(hikariDataSource);

        Log4JdbcCustomFormatter formatter = new Log4JdbcCustomFormatter();
        formatter.setLoggingType(LoggingType.MULTI_LINE);
        logDataSource.setLogFormatter(formatter);
        return logDataSource;
    }

    /**
     * mybatis 스프링 트랜잭션 연동
     * */
    @Primary
    @Bean(name = "defaultTx") //method 이름 대신 다른 이름을 사용하고 싶을때 bean=name 주입
    /**
     * Qualifier : 사용할 의존 객체를 선택할 수 있도록 해준다
     * */
    public PlatformTransactionManager transactionManager(@Qualifier("defaultDataSource") DataSource defaultDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(defaultDataSource);
        return transactionManager;
    }
}