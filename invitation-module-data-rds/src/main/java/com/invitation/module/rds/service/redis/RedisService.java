package com.invitation.module.rds.service.redis;

import java.util.List;

/**
 * SessionCallBack 사용시
 * - MULTI : 트랜잭션 시작, MULTI 이후, 명령어는 Queue에 쌓인 후, EXEC 후 실행
 * - WATCH / UNWATCH : Optimistic LOCK, WATCH 이후, UNWATCH 이전까지 한번의 EXEC, Transactional 이 아닌 다른 Command만 허용.
 *  >> ex : watch(key)  해당 트랜잭션에서 key 값에 대헤 1번만 값 변경을 허용.
 * - DISCARD : Queue에 쌓인 명령어를 삭제, Rollback
 * - EXEC : Queue 에 쌓인 명령어를 실행, Commit
 * **/

public interface RedisService<T, ID> {

    int save(ID id, List<T> list);
    int save(T entity);
    T findById(ID id);
    List<T> findAllById(ID id);
    List<T> findAll();
    boolean existsById(ID id);
    int delete(T entity);
    int deleteById(ID id);
    int deleteAll();
    int deleteAll(List<T> list);

    String getRedisKey(ID id);

}
