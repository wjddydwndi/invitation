package com.invitation.module.rds.config.database;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

public interface JpaConfiguration {
    /**
        idle-timeout                :  connection pool 에 일을 안하는 connection이 유지하는 시간
        max-life-time               : connection pool 에서 살아있을 수 있는 connection 의 최대 수명 시간
        leak-detection-threshold    : connection 이 누수 로그 메시지가 나오기 전에 connection 을 검사하여 pool에서 connection 을 내보낼 수 있는 시간; 0으로 설정시 사용하지 않음.
        validation-timeout          : valid query 를 통해 connection 이 유효한지 검사할 때 사용되는 timeout *default:5000 ms
        pool-size                   : connection pool에 유지시킬 수 있는 최대 connection 수 *default 10
        auto-commit                 : connection 이 종료되거나 pool에 반환된 때, connection에 속해잇는 transaction을 commit 할지를 결정
        connection-timeout          : connection pool 에서 connection 을 얻어오기 전까지 기다리는 최대시간, 허용가능한 wait time을 초과하면 SQL Exception 발생 *default 30000 ms (30s)
        pool-name                   : connection pool 이름을 지정함. logging 이나 JMX management console 에 표시되는 이름
   **/
    public DataSource dataSource();


    /**
     * ddl-auto options
     create : 기존 테이블 삭제 후 다시 생성 (DROP + CREATE)
     create-drop : create와 같으나 종료시점에 테이블 DROP
     update : 변경분만 반영 (운영 DB 에서는 사용 X)
     validate : 엔티티와 테이블이 정상 매핑 되었는지만 확인
     none : 사용하지 않음 ( 사실상 없는 값이지만, 관례상 none 이라 함)
     ** 운영 장비에서는 절대로 create, create-drop, update 사용하면 안된다.
     ** validate, none 사용.
     * show-sql : console에 jpa 실행 쿼리 출력
     * database-platform : JPA 데이터베이스 플랫폼 지정,
     * */
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws Exception;

    public PlatformTransactionManager platformTransactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) throws Exception;
}
