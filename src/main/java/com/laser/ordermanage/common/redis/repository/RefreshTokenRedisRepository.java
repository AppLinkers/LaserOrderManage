package com.laser.ordermanage.common.redis.repository;

import com.laser.ordermanage.common.redis.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String refreshToken);

}
