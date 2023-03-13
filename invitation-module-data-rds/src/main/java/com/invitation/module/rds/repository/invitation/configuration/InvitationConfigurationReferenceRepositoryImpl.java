package com.invitation.module.rds.repository.invitation.configuration;

import static com.invitation.module.common.model.configuration.ConfigValues.*;

import com.invitation.module.common.model.configuration.ConfigurationReference;
import com.invitation.module.rds.entity.invitation.configuration.ConfigurationReferenceEntity;
import com.invitation.module.rds.entity.invitation.configuration.InvitationConfigurationEntity;
import com.invitation.module.rds.entity.invitation.configuration.QConfigurationReferenceEntity;
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
public class InvitationConfigurationReferenceRepositoryImpl extends InvitationQuerydslRepositorySupport implements InvitationConfigurationReferenceRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QConfigurationReferenceEntity entity;

    public InvitationConfigurationReferenceRepositoryImpl(@Qualifier("invitationJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(ConfigurationReferenceEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entity = QConfigurationReferenceEntity.configurationReferenceEntity;
    }


    @Transactional(readOnly = true)
    public ConfigurationReference findMaxUpdateTime() {
        QConfigurationReferenceEntity entity = QConfigurationReferenceEntity.configurationReferenceEntity;

        return jpaQueryFactory.select(
                        Projections.fields(ConfigurationReference.class,
                        entity.seq,
                        entity.category,
                        entity.code,
                        entity.value,
                        entity.description,
                        entity.enabled,
                        entity.createTime,
                        entity.updateTime,
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.DATABASE()), "databaseName"),
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.TABLE()), "tableName")
                        ))
                .from(entity)
                .where(entity.enabled.eq(true))
                .orderBy(entity.updateTime.desc())
                .fetchFirst();
    }

    @Transactional(readOnly = true)
    public List<ConfigurationReference> findAll() {
        QConfigurationReferenceEntity entity = QConfigurationReferenceEntity.configurationReferenceEntity;

        return jpaQueryFactory.select(
                        Projections.fields(ConfigurationReference.class,
                                entity.seq,
                                entity.category,
                                entity.code,
                                entity.value,
                                entity.description,
                                entity.enabled,
                                entity.createTime,
                                entity.updateTime,
                                ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.DATABASE()), "databaseName"),
                                ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.TABLE()), "tableName")
                        ))
                .from(entity)
                .where(entity.enabled.eq(true))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }


    @Transactional(readOnly = true)
    public List<ConfigurationReference> findLastUpdateTime(Date lastTime) {
        QConfigurationReferenceEntity entity = QConfigurationReferenceEntity.configurationReferenceEntity;

        return jpaQueryFactory.select(
                        Projections.fields(ConfigurationReference.class,
                        entity.seq,
                        entity.category,
                        entity.code,
                        entity.value,
                        entity.description,
                        entity.enabled,
                        entity.createTime,
                        entity.updateTime,
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.DATABASE()), "databaseName"),
                        ExpressionUtils.as(Expressions.asString(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.TABLE()), "tableName")
                ))
                .from(entity)
                .where(entity.updateTime.gt(lastTime).and(entity.enabled.eq(true)))
                .orderBy(entity.updateTime.desc())
                .fetch();
    }
}
