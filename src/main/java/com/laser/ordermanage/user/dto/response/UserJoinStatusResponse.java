package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserJoinStatusResponse {

    private String email;

    private LocalDateTime createdAt;

    private final String status;

    @Builder(builderMethodName = "builderWithUserEntity", buildMethodName = "buildWithUserEntity")
    public UserJoinStatusResponse(UserEntity userEntity, JoinStatus status) {
        this.email = userEntity.getEmail();
        this.createdAt = userEntity.getCreatedAt();
        this.status = status.getCode();
    }

    @Builder(builderMethodName = "builderWithOutUserEntity", buildMethodName = "buildWithOutUserEntity")
    public UserJoinStatusResponse(JoinStatus status) {
        this.status = status.getCode();
    }
}
