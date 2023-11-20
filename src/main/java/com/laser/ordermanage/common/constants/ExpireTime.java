package com.laser.ordermanage.common.constants;

public class ExpireTime {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;               //60분 -> 1000L -> 1초
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;     //7일
    public static final long REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS_AND_COOKIE = REFRESH_TOKEN_EXPIRE_TIME / 1000L; // Redis, Cookie 에서 1L -> 1 초

    public static final long VERIFY_CODE_EXPIRE_TIME = 15 * 60L;  // 15분
}
