package com.laser.ordermanage.common.security.jwt.setup;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtBuilder {

    private final JwtProvider jwtProvider;

    public String refreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "ROLE_CUSTOMER", "refresh", new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String invalidJwtBuild() {
        return "invalid.jwt.token";
    }

    public String unauthorizedAccessJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "", "access", new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String unauthorizedRefreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "", "refresh", new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String expiredAccessJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", "ROLE_CUSTOMER", "access", expiredDate, ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String expiredRefreshJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", "ROLE_CUSTOMER", "refresh", expiredDate, ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }
}
