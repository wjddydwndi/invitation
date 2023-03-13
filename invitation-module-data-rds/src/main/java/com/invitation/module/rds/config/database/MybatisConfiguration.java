package com.invitation.module.rds.config.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import javax.sql.DataSource;

public interface MybatisConfiguration {

    public DataSource dataSource();

    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception;

    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception;
}
