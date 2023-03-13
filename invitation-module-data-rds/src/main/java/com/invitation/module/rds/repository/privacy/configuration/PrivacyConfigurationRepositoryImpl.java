package com.invitation.module.rds.repository.privacy.configuration;

import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.rds.entity.invitation.configuration.InvitationConfigurationEntity;
import com.invitation.module.rds.entity.invitation.configuration.QInvitationConfigurationEntity;
import com.invitation.module.rds.entity.privacy.configuration.QPrivateConfigurationEntity;
import com.invitation.module.rds.repository.PrivacyQuerydslRepositorySupport;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Repository
public class PrivacyConfigurationRepositoryImpl extends PrivacyQuerydslRepositorySupport implements PrivacyConfigurationRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPrivateConfigurationEntity entity;

    public PrivacyConfigurationRepositoryImpl(@Qualifier("privacyJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(InvitationConfigurationEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entity = QPrivateConfigurationEntity.privateConfigurationEntity;
    }

    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public Configuration findMaxUpdateTime() {

        return jpaQueryFactory.select(
                        Projections.fields(Configuration.class,
                        entity.seq,
                        entity.category,
                        entity.code,
                        entity.value,
                        entity.description,
                        entity.enabled,
                        entity.isReference,
                        entity.createTime,
                        entity.updateTime,
                        ExpressionUtils.as(Expressions.asString("privacy"), "databaseName"),
                        ExpressionUtils.as(Expressions.asString("t_configuration"), "tableName")
                        ))
                .from(entity)
                .where(entity.enabled.eq(true))
                .orderBy(entity.updateTime.desc())
                .fetchFirst();
    }

    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public List<Configuration> findAll() {

        return jpaQueryFactory.select(
                        Projections.fields(Configuration.class,
                                entity.seq,
                                entity.category,
                                entity.code,
                                entity.value,
                                entity.description,
                                entity.enabled,
                                entity.isReference,
                                entity.createTime,
                                entity.updateTime,
                                ExpressionUtils.as(Expressions.asString("privacy"), "databaseName"),
                                ExpressionUtils.as(Expressions.asString("t_configuration"), "tableName")
                        ))
                .from(entity)
                .where(entity.enabled.eq(true))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }


    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public List<Configuration> findLastUpdateTime(Date lastTime) {

        return jpaQueryFactory.select(
                        Projections.fields(Configuration.class,
                        entity.seq,
                        entity.category,
                        entity.code,
                        entity.value,
                        entity.description,
                        entity.enabled,
                        entity.isReference,
                        entity.createTime,
                        entity.updateTime,
                        ExpressionUtils.as(Expressions.asString("privacy"), "databaseName"),
                        ExpressionUtils.as(Expressions.asString("t_configuration"), "tableName")
                ))
                .from(entity)
                .where(entity.updateTime.gt(lastTime).and(entity.enabled.eq(true)))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }
}
