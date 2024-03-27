package com.laser.ordermanage.user.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = UserEntityRepository.class)
public class UserEntityRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    protected UserEntityRepository userEntityRepository;

    @Test
    public void findFirstByEmail_존재_O() {
        // given
        final UserEntity expectedUser = UserEntityBuilder.build();

        // when
        final Optional<UserEntity> optionalUser = userEntityRepository.findFirstByEmail(expectedUser.getEmail());

        // then
        Assertions.assertThat(optionalUser.isPresent()).isTrue();
        optionalUser.ifPresent(
                actualUser -> {
                    Assertions.assertThat(actualUser.getId()).isEqualTo(2L);
                    Assertions.assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
                    Assertions.assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
                    Assertions.assertThat(actualUser.getAuthority()).isEqualTo(expectedUser.getAuthority());
                    Assertions.assertThat(actualUser.getPhone()).isEqualTo(expectedUser.getPhone());
                    Assertions.assertThat(actualUser.getAddress().getZipCode()).isEqualTo(expectedUser.getAddress().getZipCode());
                    Assertions.assertThat(actualUser.getAddress().getAddress()).isEqualTo(expectedUser.getAddress().getAddress());
                    Assertions.assertThat(actualUser.getAddress().getDetailAddress()).isEqualTo(expectedUser.getAddress().getDetailAddress());
                }
        );
    }

    @Test
    public void findFirstByEmail_존재_X() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // when
        final Optional<UserEntity> optionalUserEntity = userEntityRepository.findFirstByEmail(unknownUserEmail);

        // then
        Assertions.assertThat(optionalUserEntity.isEmpty()).isTrue();
    }

    @Test
    public void findEmailByNameAndPhone_존재_O() {
        // given
        final String expectedName = "고객 이름 1";
        final String expectedPhone = "01022221111";

        // when
        final List<GetUserEmailResponse> actualResponseList = userEntityRepository.findEmailByNameAndPhone(expectedName, expectedPhone);

        // then
        Assertions.assertThat(actualResponseList.size()).isEqualTo(2);

        Assertions.assertThat(actualResponseList.get(0).name()).isEqualTo(expectedName);
        Assertions.assertThat(actualResponseList.get(0).email()).isEqualTo("user1-copy@gmail.com");

        Assertions.assertThat(actualResponseList.get(1).name()).isEqualTo(expectedName);
        Assertions.assertThat(actualResponseList.get(1).email()).isEqualTo("user1@gmail.com");
    }

    @Test
    public void findEmailByNameAndPhone_존재_X() {
        // given
        final String unknownUserName = "존재하지 않는 사용자";
        final String unknownUserPhone = "12121212121";

        // when
        final List<GetUserEmailResponse> actualResponseList = userEntityRepository.findEmailByNameAndPhone(unknownUserName, unknownUserPhone);

        // then
        Assertions.assertThat(actualResponseList.size()).isEqualTo(0);
    }

    @Test
    public void findEmailByNameAndPhone_존재_X_By_name() {
        // given
        final String unknownUserName = "존재하지 않는 사용자";
        final String userPhone = "01011111111";

        // when
        final List<GetUserEmailResponse> actualResponseList = userEntityRepository.findEmailByNameAndPhone(unknownUserName, userPhone);

        // then
        Assertions.assertThat(actualResponseList.size()).isEqualTo(0);
    }

    @Test
    public void findEmailByNameAndPhone_존재_X_By_phone() {
        // given
        final String userName = "고객 이름 1";
        final String unknownUserPhone = "12121212121";

        // when
        final List<GetUserEmailResponse> actualResponseList = userEntityRepository.findEmailByNameAndPhone(userName, unknownUserPhone);

        // then
        Assertions.assertThat(actualResponseList.size()).isEqualTo(0);
    }

    @Test
    public void findUserAccountByEmail_존재_O() {
        // given
        final UserEntity expectedUser = UserEntityBuilder.build();
        final GetUserAccountResponse expectedResponse = GetUserAccountResponse.builder()
                .email(expectedUser.getEmail())
                .name(expectedUser.getName())
                .phone(expectedUser.getPhone())
                .zipCode(expectedUser.getAddress().getZipCode())
                .address(expectedUser.getAddress().getAddress())
                .detailAddress(expectedUser.getAddress().getDetailAddress())
                .emailNotification(expectedUser.getEmailNotification())
                .build();

        // when
        final GetUserAccountResponse actualResponse = userEntityRepository.findUserAccountByEmail(expectedUser.getEmail());

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void findUserAccountByEmail_존재_X() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // when
        final GetUserAccountResponse actualResponse = userEntityRepository.findUserAccountByEmail(unknownUserEmail);

        // then
        Assertions.assertThat(actualResponse).isNull();
    }

}
