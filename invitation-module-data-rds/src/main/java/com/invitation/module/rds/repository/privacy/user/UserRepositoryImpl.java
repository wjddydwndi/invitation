package com.invitation.module.rds.repository.privacy.user;

import com.invitation.module.common.model.user.User;
import com.invitation.module.rds.entity.privacy.user.QUserEntity;
import com.invitation.module.rds.entity.privacy.user.UserEntity;
import com.invitation.module.rds.repository.PrivacyQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
public class UserRepositoryImpl extends PrivacyQuerydslRepositorySupport implements UserRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final QUserEntity entity;

    public UserRepositoryImpl(@Qualifier("privacyJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(UserEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entity = QUserEntity.userEntity;
    }

    @Override
    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public List<User> findAll() {
        QUserEntity entity = QUserEntity.userEntity;

        return jpaQueryFactory.select(
                        Projections.fields(User.class,
                                entity.seq,
                                entity.id,
                                entity.email,
                                entity.password,
                                entity.firstName,
                                entity.lastName,
                                entity.gender,
                                entity.birth,
                                entity.nationality,
                                entity.cellNo,
                                entity.updateAt,
                                entity.createAt
                        ))
                .from(entity)
                .orderBy(entity.createAt.desc())
                .fetch();
    }

    @Override
    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public User findById(String userId) {
        QUserEntity entity = QUserEntity.userEntity;

        return jpaQueryFactory.select(
                        Projections.fields(User.class,
                                entity.seq,
                                entity.id,
                                entity.email,
                                entity.password,
                                entity.firstName,
                                entity.lastName,
                                entity.gender,
                                entity.birth,
                                entity.nationality,
                                entity.cellNo,
                                entity.updateAt,
                                entity.createAt
                                ))
                .from(entity)
                .where(entity.id.eq(userId))
                .fetchOne();
    }

    @Override
    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public User findByEmail(String email) {
        QUserEntity entity = QUserEntity.userEntity;

        return jpaQueryFactory.select(
                        Projections.fields(User.class,
                                entity.seq,
                                entity.id,
                                entity.email,
                                entity.password,
                                entity.firstName,
                                entity.lastName,
                                entity.gender,
                                entity.birth,
                                entity.nationality,
                                entity.cellNo,
                                entity.updateAt,
                                entity.createAt
                        ))
                .from(entity)
                .where(entity.email.eq(email))
                .fetchOne();
    }
}
