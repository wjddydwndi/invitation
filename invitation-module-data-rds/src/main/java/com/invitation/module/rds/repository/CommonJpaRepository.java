package com.invitation.module.rds.repository;/*
package com.invitation.module.api.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

//https://okky.kr/articles/950902
public interface CommonJpaRepository<T, ID> extends JpaRepository<T, ID> {

    @Override Optional<T> findById(ID id);

    @Override List<T> findAllById(Iterable<ID> ids);

    @Override List<T> findAll();

    @Override <S extends T> Optional<S> findOne(Example<S> example);

    @Override <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override Page<T> findAll(Pageable pageable);
}
*/
