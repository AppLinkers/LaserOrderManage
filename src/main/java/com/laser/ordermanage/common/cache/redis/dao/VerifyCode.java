package com.laser.ordermanage.common.cache.redis.dao;

import com.laser.ordermanage.common.constants.ExpireTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verifyCode", timeToLive = ExpireTime.VERIFY_CODE_EXPIRE_TIME)
public class VerifyCode {

    @Id
    private String email;

    private String code;
}
