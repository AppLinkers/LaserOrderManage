package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
