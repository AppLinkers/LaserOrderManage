package com.laser.ordermanage.common.security.jwt.setup;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtBuilder {

    private final JwtProvider jwtProvider;

    public String accessJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String refreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String changePasswordJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String invalidJwtBuild() {
        return "invalid.jwt.token";
    }

    public String unauthorizedAccessJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "", JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String unauthorizedRefreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "", JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String unauthorizedChangePasswordJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", "", JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String expiredAccessJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_ACCESS, expiredDate, ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String expiredRefreshJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_REFRESH, expiredDate, ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String expiredChangePasswordJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_CHANGE_PASSWORD, expiredDate, ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String accessJwtOfUnknownUserBuild() {
        return jwtProvider.generateJWT("unknwon-user@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String changePasswordJwtOfUnknownUserBuild() {
        return jwtProvider.generateJWT("unknwon-user@gmail.com", Role.ROLE_CUSTOMER.name(), JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }
}
