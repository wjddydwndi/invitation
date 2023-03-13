package com.invitation.module.rds.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement // 트랜잭션 범위를 활성화
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(// repository package 연결 설정
        basePackages = {"com.invitation.module.rds.repository.invitation"},
        entityManagerFactoryRef = "invitationEntityManagerFactory",
        transactionManagerRef = "invitationPlatformTransactionManager")
/*@MapperScan(
        basePackages = {"com.invitation.module.api", "com.invitation.module.batch"}
        //sqlSessionFactoryRef = "sqlSessionFactory"
)*/
public class DatabaseConfigInvitation implements JpaConfiguration {

    /**
     * DataSource 프로퍼티 설정
     * **/
    @Value(value="${invitation.datasource.url}")
    private String url;

    @Value(value="${invitation.datasource.username}")
    private String username;

    @Value(value="${invitation.datasource.password}")
    private String password;

    @Value(value="${invitation.datasource.driver-class-name}")
    private String driverClassName;

    @Value(value="${invitation.datasource.hikari.pool-size}")
    private int poolSize;

    @Value(value="${invitation.datasource.hikari.enkey}")
    private String encodingKey;

    @Value(value="${invitation.datasource.hikari.idle-timeout}")
    private int idleTimeout;

    @Value(value="${invitation.datasource.hikari.max-life-time}")
    private int maxLifeTime;

    @Value(value="${invitation.datasource.hikari.leak-detection-threshold}")
    private int leakDetectionThreshold;

    @Value(value="${invitation.datasource.hikari.validation-timeout}")
    private int validationTimeout;

    @Value(value="${spring.jpa.database-platform}")
    private String databasePlatform;

    @Value(value="${spring.jpa.show-sql}")
    private String showSql;

    @Value(value="${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;


    @Override
    @Primary
    @Bean(name="invitationDataSource")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        // options
        hikariConfig.setIdleTimeout(idleTimeout);                                   //  connection pool 에 일을 안하는 connection이 유지하는 시간
        hikariConfig.setMaxLifetime(maxLifeTime);                                   //  connection pool 에서 살아있을 수 있는 connection 의 최대 수명 시간
        hikariConfig.setLeakDetectionThreshold(leakDetectionThreshold);             //  connection 이 누수 로그 메시지가 나오기 전에 connection 을 검사하여 pool에서 connection 을 내보낼 수 있는 시간; 0으로 설정시 사용하지 않음.
        hikariConfig.setValidationTimeout(validationTimeout);                       //  valid query 를 통해 connection 이 유효한지 검사할 때 사용되는 timeout *default:5000 ms
        hikariConfig.setMaximumPoolSize(poolSize);                                  //  connection pool에 유지시킬 수 있는 최대 connection 수 *default 10
        hikariConfig.setAutoCommit(true);                                           //  connection 이 종료되거나 pool에 반환된 때, connection에 속해잇는 transaction을 commit 할지를 결정
        hikariConfig.setConnectionTimeout(3000);                                    //  connection pool 에서 connection 을 얻어오기 전까지 기다리는 최대시간, 허용가능한 wait time을 초과하면 SQL Exception 발생 *default 30000 ms (30s)
        hikariConfig.setPoolName("invitation-invitation-dbpool");                      //  connection pool 이름을 지정함. logging 이나 JMX management console 에 표시되는 이름
        hikariConfig.setConnectionInitSql("SET @enckey = '".concat(encodingKey).concat("'"));
        hikariConfig.setConnectionTestQuery("SELECT 1");

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return hikariDataSource;
    }

    @Override
    @Primary
    @Bean(name="invitationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("invitationDataSource") DataSource dataSource) throws Exception {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.invitation.module.rds.entity.invitation");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("invitationEntityManager");
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.dialect", databasePlatform);

        localContainerEntityManagerFactoryBean.setJpaProperties(properties);

        return localContainerEntityManagerFactoryBean;
    }

    @Override
    @Primary
    @Bean(name="invitationPlatformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("invitationEntityManagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
        transactionManager.setNestedTransactionAllowed(true);

        return transactionManager;
    }



}
