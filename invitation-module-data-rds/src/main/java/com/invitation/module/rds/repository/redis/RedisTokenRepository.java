package com.invitation.module.rds.repository.redis;

import com.invitation.module.common.model.user.Token;
import org.springframework.data.repository.CrudRepository;

public interface RedisTokenRepository extends CrudRepository<Token, String> { }
