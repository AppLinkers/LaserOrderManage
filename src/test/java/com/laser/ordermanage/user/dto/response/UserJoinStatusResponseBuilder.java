package com.laser.ordermanage.user.dto.response;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;

public class UserJoinStatusResponseBuilder {
    public static UserJoinStatusResponse buildPossibleWithOutUserEntity() {
        return UserJoinStatusResponse.builderWithOutUserEntity()
                .status(JoinStatus.POSSIBLE)
                .buildWithOutUserEntity();
    }

    public static UserJoinStatusResponse buildImpossibleWithUserEntity(UserEntity user) {
        return UserJoinStatusResponse.builderWithUserEntity()
                .userEntity(user)
                .status(JoinStatus.IMPOSSIBLE)
                .buildWithUserEntity();
    }

    public static UserJoinStatusResponse buildCompletedWithUserEntity(UserEntity user) {
        return UserJoinStatusResponse.builderWithUserEntity()
                .userEntity(user)
                .status(JoinStatus.COMPLETED)
                .buildWithUserEntity();
    }

    public static void assertUserJoinStatusResponseWithCreatedAt(UserJoinStatusResponse actualResponse, UserJoinStatusResponse expectedResponse, LocalDateTime expectedCreatedAt) {
        Assertions.assertThat(actualResponse.email()).isEqualTo(expectedResponse.email());
        Assertions.assertThat(actualResponse.createdAt()).isEqualTo(expectedCreatedAt);
        Assertions.assertThat(actualResponse.status()).isEqualTo(expectedResponse.status());
    }

    public static void assertUserJoinStatusResponseWithOutCreatedAt(UserJoinStatusResponse actualResponse, UserJoinStatusResponse expectedResponse) {
        Assertions.assertThat(actualResponse.email()).isEqualTo(expectedResponse.email());
        Assertions.assertThat(actualResponse.status()).isEqualTo(expectedResponse.status());
    }
}
