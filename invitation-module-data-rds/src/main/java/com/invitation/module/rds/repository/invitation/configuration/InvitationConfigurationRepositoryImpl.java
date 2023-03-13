package com.invitation.module.rds.repository.invitation.configuration;

import static com.invitation.module.common.model.configuration.ConfigValues.*;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.rds.entity.invitation.configuration.InvitationConfigurationEntity;
import com.invitation.module.rds.entity.invitation.configuration.QInvitationConfigurationEntity;
import com.invitation.module.rds.repository.InvitationQuerydslRepositorySupport;
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
public class InvitationConfigurationRepositoryImpl extends InvitationQuerydslRepositorySupport implements InvitationConfigurationRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QInvitationConfigurationEntity entity;

    public InvitationConfigurationRepositoryImpl(@Qualifier("invitationJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(InvitationConfigurationEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entity = QInvitationConfigurationEntity.invitationConfigurationEntity;
    }

    public List<Configuration> findMaxUpdateTime() {
        QInvitationConfigurationEntity entity = QInvitationConfigurationEntity.invitationConfigurationEntity;

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
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.DATABASE()), "databaseName"),
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.TABLE()), "tableName")
                        ))
                .where(entity.enabled.eq(true))
                .from(entity)
                .orderBy(entity.updateTime.desc())
                .fetch();
    }


    @Transactional(readOnly = true)
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
                                ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.DATABASE()), "databaseName"),
                                ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.TABLE()), "tableName")
                        ))
                .from(entity)
                .where(entity.enabled.eq(true))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }


    @Transactional(readOnly = true)
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
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.DATABASE()), "databaseName"),
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION.TABLE()), "tableName")
                ))
                .from(entity)
                .where(entity.updateTime.gt(lastTime).and(entity.enabled.eq(true)))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }
}
