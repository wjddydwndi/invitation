package com.invitation.module.rds.config.database;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
@EnableJpaAuditing
public class QuerydslConfiguration {
    /**
     * JPQL VS QueryDSL
     * JPQL 은 오류시 컴파일 단계에서 발견할 수 없다. 때문에 실제 코드가 작동하는 순간까지 에러를 발견할 수 없음.
     * QueryDSL 자바 코드로 작성하므로 컴파일 단계에서 에러를 발견할 수 있음.
     * **/

    /**
     * JPAQueryFactory 를 필드로 제공하면 동시성 문제는 JPAQueryFactory를 생성할 때 제공하는 EntityManager에 달려있다.
     * 스프링 프레임워크는 여러 쓰레드에서 동시에 같은 EntityManager 에 접근해도,
     * 트랜잭션마다 별도의 영속성 컨텍스트를 제공하기 때문에 동시성 문제는 걱정하지 않아도 됨.*/
    @PersistenceContext(unitName = "invitationEntityManager")
    private EntityManager invitationEntityManager;

    @PersistenceContext(unitName = "privacyEntityManager")
    private EntityManager privacyEntityManager;

    @Bean(name="invitationJpaQueryFactory")
    public JPAQueryFactory invitationJpaQueryFactory() {
        return new JPAQueryFactory(invitationEntityManager);
    }

    @Bean(name="privacyJpaQueryFactory")
    public JPAQueryFactory privacyJpaQueryFactory() {
        return new JPAQueryFactory(privacyEntityManager);
    }
}
