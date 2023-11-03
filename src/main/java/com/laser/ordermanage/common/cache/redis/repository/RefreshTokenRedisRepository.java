package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String refreshToken);

}
