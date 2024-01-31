package com.laser.ordermanage.user.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = UserEntityRepository.class)
public class UserEntityRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    protected UserEntityRepository userEntityRepository;

    @Test
    public void findFirstByEmail_존재_O() {
        // given
        UserEntity userEntity = UserEntityBuilder.build();

        // when
        final Optional<UserEntity> optionalUserEntity = userEntityRepository.findFirstByEmail(userEntity.getEmail());

        // then
        Assertions.assertThat(optionalUserEntity.isPresent()).isTrue();
        optionalUserEntity.ifPresent(
                user -> {
                    Assertions.assertThat(user.getId()).isEqualTo(2L);
                    Assertions.assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
                    Assertions.assertThat(user.getRole()).isEqualTo(userEntity.getRole());
                    Assertions.assertThat(user.getPhone()).isEqualTo(userEntity.getPhone());
                    Assertions.assertThat(user.getAddress().getZipCode()).isEqualTo(userEntity.getAddress().getZipCode());
                    Assertions.assertThat(user.getAddress().getDetailAddress()).isEqualTo(userEntity.getAddress().getDetailAddress());
                    Assertions.assertThat(user.getAddress().getAddress()).isEqualTo(userEntity.getAddress().getAddress());
                }
        );
    }

    @Test
    public void findFirstByEmail_존재_X() {
        // given
        String invalidUserEmail = "invalid-user@gmail.com";

        // when
        final Optional<UserEntity> optionalUserEntity = userEntityRepository.findFirstByEmail(invalidUserEmail);

        // then
        Assertions.assertThat(optionalUserEntity.isEmpty()).isTrue();
    }
}
