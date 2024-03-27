package com.laser.ordermanage.common.security.jwt.setup;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtBuilder {

    private final JwtProvider jwtProvider;

    public String accessJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String accessJwtBuildOfUser2() {
        return jwtProvider.generateJWT("user2@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String accessJwtBuildOfFactory() {
        return jwtProvider.generateJWT("admin@kumoh.org", List.of(Role.ROLE_FACTORY.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String refreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String changePasswordJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String invalidJwtBuild() {
        return "invalid.jwt.token";
    }

    public String unauthorizedAccessJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", new ArrayList<>(), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String unauthorizedRefreshJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", new ArrayList<>(), JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String unauthorizedChangePasswordJwtBuild() {
        return jwtProvider.generateJWT("user1@gmail.com", new ArrayList<>(), JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String expiredAccessJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_ACCESS, expiredDate, ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String expiredRefreshJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_REFRESH, expiredDate, ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String expiredChangePasswordJwtBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
        return jwtProvider.generateJWT("user1@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_CHANGE_PASSWORD, expiredDate, ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String accessJwtOfUnknownUserBuild() {
        return jwtProvider.generateJWT("unknwon-user@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String changePasswordJwtOfUnknownUserBuild() {
        return jwtProvider.generateJWT("unknwon-user@gmail.com", List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()), JwtProvider.TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }
}
