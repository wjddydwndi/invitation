package com.invitation.module.rds.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class PrivacyQuerydslRepositorySupport extends QuerydslRepositorySupport {

    public PrivacyQuerydslRepositorySupport(Class<?> domainClass) {
        super(domainClass);
    }
}
