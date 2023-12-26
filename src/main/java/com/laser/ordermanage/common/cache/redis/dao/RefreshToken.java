package com.laser.ordermanage.common.cache.redis.dao;

import com.laser.ordermanage.common.constants.ExpireTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = ExpireTime.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS)
public class RefreshToken {

    @Id
    private String id;

    private String ip;

    private String role;

    @Indexed
    private String refreshToken;
}
