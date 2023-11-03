package com.laser.ordermanage.common.cache.redis.repository;

import com.laser.ordermanage.common.cache.redis.dao.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRedisRepository extends CrudRepository<BlackList, String> {

    Optional<BlackList> findByAccessToken(String accessToken);

}
