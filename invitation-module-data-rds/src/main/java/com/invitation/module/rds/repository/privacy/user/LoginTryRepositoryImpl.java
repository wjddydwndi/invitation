package com.invitation.module.rds.repository.privacy.user;

import com.invitation.module.common.model.user.LoginTry;
import com.invitation.module.rds.entity.privacy.user.QLoginTryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LoginTryRepositoryImpl implements LoginTryRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final QLoginTryEntity qLoginTryEntity;


    @Override
    @Transactional(value = "privacyPlatformTransactionManager", readOnly = true)
    public long updateLoginTry(String userId) {
        QLoginTryEntity entity = QLoginTryEntity.loginTryEntity;

        return jpaQueryFactory.update(entity).set(entity.retryCount, entity.retryCount.add(1))
                .where(entity.userId.eq(userId))
                .execute();
    }
}
