package com.laser.ordermanage.common.redis.repository;

import com.laser.ordermanage.common.redis.domain.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRedisRepository extends CrudRepository<BlackList, String> {

    Optional<BlackList> findByAccessToken(String accessToken);

}
