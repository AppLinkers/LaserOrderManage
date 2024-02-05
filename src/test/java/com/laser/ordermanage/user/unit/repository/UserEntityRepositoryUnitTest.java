package com.laser.ordermanage.user.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetUserAccountResponse;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
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
                    Assertions.assertThat(actualUser.getPhone()).isEqualTo(expectedUser.getPhone());
                    Assertions.assertThat(actualUser.getAddress().getZipCode()).isEqualTo(expectedUser.getAddress().getZipCode());
                    Assertions.assertThat(actualUser.getAddress().getDetailAddress()).isEqualTo(expectedUser.getAddress().getDetailAddress());
                    Assertions.assertThat(actualUser.getAddress().getAddress()).isEqualTo(expectedUser.getAddress().getAddress());
                }
        );
    }

    @Test
    public void findFirstByEmail_존재_X() {
        // given
        final String invalidUserEmail = "invalid-user@gmail.com";

        // when
        final Optional<UserEntity> optionalUserEntity = userEntityRepository.findFirstByEmail(invalidUserEmail);

        // then
        Assertions.assertThat(optionalUserEntity.isEmpty()).isTrue();
    }

    @Test
    public void findEmailByNameAndPhone_존재_O() {
        // given
        final String name = "고객 이름 1";
        final String phone = "01011111111";

        // when
        final List<GetUserEmailResponse> userEmailList = userEntityRepository.findEmailByNameAndPhone(name, phone);

        // then
        Assertions.assertThat(userEmailList.size()).isEqualTo(2);

        Assertions.assertThat(userEmailList.get(0).name()).isEqualTo(name);
        Assertions.assertThat(userEmailList.get(0).email()).isEqualTo("user1-copy@gmail.com");

        Assertions.assertThat(userEmailList.get(1).name()).isEqualTo(name);
        Assertions.assertThat(userEmailList.get(1).email()).isEqualTo("user1@gmail.com");
    }

    @Test
    public void findEmailByNameAndPhone_존재_X() {
        // given
        final String invalidUserName = "존재하지 않는 사용자";
        final String invalidUserPhone = "12121212121";

        // when
        final List<GetUserEmailResponse> userEmailList = userEntityRepository.findEmailByNameAndPhone(invalidUserName, invalidUserPhone);

        // then
        Assertions.assertThat(userEmailList.size()).isEqualTo(0);
    }

    @Test
    public void findEmailByNameAndPhone_존재_X_By_name() {
        // given
        final String invalidUserName = "존재하지 않는 사용자";
        final String userPhone = "01011111111";

        // when
        final List<GetUserEmailResponse> userEmailList = userEntityRepository.findEmailByNameAndPhone(invalidUserName, userPhone);

        // then
        Assertions.assertThat(userEmailList.size()).isEqualTo(0);
    }

    @Test
    public void findEmailByNameAndPhone_존재_X_By_phone() {
        // given
        final String userName = "고객 이름 1";
        final String invalidUserPhone = "12121212121";

        // when
        final List<GetUserEmailResponse> userEmailList = userEntityRepository.findEmailByNameAndPhone(userName, invalidUserPhone);

        // then
        Assertions.assertThat(userEmailList.size()).isEqualTo(0);
    }

    @Test
    public void findUserAccountByFactory_존재_O() {
        // given
        final FactoryGetUserAccountResponse expectedFactory = FactoryGetUserAccountResponse.builder()
                .email("admin@kumoh.org")
                .companyName("금오 M.T")
                .representative("정연근")
                .phone("01011111111")
                .fax("0314310413")
                .zipCode("11111")
                .address("factory1_address")
                .detailAddress("factory1_detail_address")
                .emailNotification(Boolean.TRUE)
                .build();

        // when
        final FactoryGetUserAccountResponse actualFactory = userEntityRepository.findUserAccountByFactory(expectedFactory.email());

        // then
        Assertions.assertThat(actualFactory).isEqualTo(expectedFactory);
    }

    @Test
    public void findUserAccountByFactory_존재_X_By_Invalid_User() {
        // given
        final String invalidUserEmail = "존재하지 않는 사용자";

        // when
        final FactoryGetUserAccountResponse actualFactory = userEntityRepository.findUserAccountByFactory(invalidUserEmail);

        // then
        Assertions.assertThat(actualFactory).isNull();
    }

    @Test
    public void findUserAccountByFactory_존재_X_By_Customer_User() {
        // given
        final String customerUserEmail = "user1@gmail.com";

        // when
        final FactoryGetUserAccountResponse actualFactory = userEntityRepository.findUserAccountByFactory(customerUserEmail);

        // then
        Assertions.assertThat(actualFactory).isNull();
    }

    @Test
    public void findUserAccountByCustomer_존재_O() {
        // given
        final CustomerGetUserAccountResponse expectedCustomer = CustomerGetUserAccountResponse.builder()
                .email("user1@gmail.com")
                .name("고객 이름 1")
                .phone("01011111111")
                .zipCode("11111")
                .address("user1_address")
                .detailAddress("user1_detail_address")
                .companyName("고객 회사 이름 1")
                .emailNotification(Boolean.TRUE)
                .build();

        // when
        final CustomerGetUserAccountResponse actualCustomer = userEntityRepository.findUserAccountByCustomer(expectedCustomer.email());

        // then
        Assertions.assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }

    @Test
    public void findUserAccountByCustomer_존재_X_By_Invalid_User() {
        // given
        final String invalidUserEmail = "존재하지 않는 사용자";

        // when
        final CustomerGetUserAccountResponse actualCustomer = userEntityRepository.findUserAccountByCustomer(invalidUserEmail);

        // then
        Assertions.assertThat(actualCustomer).isNull();
    }

    @Test
    public void findUserAccountByCustomer_존재_X_By_Factory_User() {
        // given
        final String factoryUserEmail = "admin@kumoh.org";

        // when
        final CustomerGetUserAccountResponse actualCustomer = userEntityRepository.findUserAccountByCustomer(factoryUserEmail);

        // then
        Assertions.assertThat(actualCustomer).isNull();
    }
}
