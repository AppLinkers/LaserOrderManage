package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.ChangePasswordToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChangePasswordTokenRedisRepository extends CrudRepository<ChangePasswordToken, String> {

    Optional<ChangePasswordToken> findByChangePasswordToken(String changePasswordToken);

}
