package com.invitation.module.rds.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class InvitationQuerydslRepositorySupport extends QuerydslRepositorySupport {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public InvitationQuerydslRepositorySupport(Class<?> domainClass) {
        super(domainClass);
    }
}
