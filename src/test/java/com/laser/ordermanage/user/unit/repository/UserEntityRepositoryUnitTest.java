package com.laser.ordermanage.user.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponseBuilder;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponseBuilder;
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
                    UserEntityBuilder.assertUserEntity(actualUser, expectedUser);
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
        final String customerName = "고객 이름 1";
        final String customerPhone = "01022221111";
        final List<GetUserEmailResponse> expectedResponse = GetUserEmailResponseBuilder.buildListForCustomer();

        // when
        final List<GetUserEmailResponse> actualResponse = userEntityRepository.findEmailByNameAndPhone(customerName, customerPhone);

        // then
        Assertions.assertThat(actualResponse).hasSameElementsAs(expectedResponse);
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
        final GetUserAccountResponse expectedResponse = GetUserAccountResponseBuilder.build();

        // when
        final GetUserAccountResponse actualResponse = userEntityRepository.findUserAccountByEmail(expectedResponse.email());

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
