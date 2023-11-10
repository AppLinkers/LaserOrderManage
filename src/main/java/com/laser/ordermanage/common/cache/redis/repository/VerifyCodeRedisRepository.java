package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import org.springframework.data.repository.CrudRepository;

public interface VerifyCodeRedisRepository extends CrudRepository<VerifyCode, String> {
}
