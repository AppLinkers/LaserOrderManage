package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.ChangePasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ChangePasswordTokenRedisRepository extends CrudRepository<ChangePasswordToken, String> {
}
