package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public record UserJoinStatusResponse(
        String email,
        LocalDateTime createdAt,
        String status
) {
    @Builder(builderMethodName = "builderWithUserEntity", buildMethodName = "buildWithUserEntity")
    public UserJoinStatusResponse (UserEntity userEntity, JoinStatus status) {
        this(
                userEntity.getEmail(),
                userEntity.getCreatedAt(),
                status.getCode()
        );
    }

    @Builder(builderMethodName = "builderWithOutUserEntity", buildMethodName = "buildWithOutUserEntity")
    public UserJoinStatusResponse (JoinStatus status) {
        this(
                null,
                null,
                status.getCode()
        );
    }
}
