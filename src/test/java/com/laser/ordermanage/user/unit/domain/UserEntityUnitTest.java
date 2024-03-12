package com.laser.ordermanage.user.unit.domain;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserEntityUnitTest {

    @Test
    public void changePassword() {
        // given
        final UserEntity actualUser = UserEntityBuilder.build();
        final String expectedPassword = "newPassword";

        // when
        actualUser.changePassword(expectedPassword);

        // then
        Assertions.assertThat(actualUser.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    public void changeEmailNotification() {
        // given
        final UserEntity actualUser = UserEntityBuilder.build();
        final Boolean expectedEmailNotification = Boolean.FALSE;

        // when
        actualUser.changeEmailNotification(expectedEmailNotification);

        // then
        Assertions.assertThat(actualUser.getEmailNotification()).isEqualTo(expectedEmailNotification);
    }

    @Test
    public void updateProperties() {
        // given
        final UserEntity actualUser = UserEntityBuilder.build();
        final UpdateUserAccountRequest expectedUserAccount = new UpdateUserAccountRequest("고객 신규 이름 1", "01011112222", "22222", "user1_address2", "user1_detail_address2");

        // when
        actualUser.updateProperties(expectedUserAccount);

        // then
        Assertions.assertThat(actualUser.getName()).isEqualTo(expectedUserAccount.name());
        Assertions.assertThat(actualUser.getPhone()).isEqualTo(expectedUserAccount.phone());
        Assertions.assertThat(actualUser.getAddress().getZipCode()).isEqualTo(expectedUserAccount.zipCode());
        Assertions.assertThat(actualUser.getAddress().getAddress()).isEqualTo(expectedUserAccount.address());
        Assertions.assertThat(actualUser.getAddress().getDetailAddress()).isEqualTo(expectedUserAccount.detailAddress());
    }

    @Test
    public void extendsUserDetails() {
        // given
        final UserEntity actualUser = UserEntityBuilder.build();

        // then
        Assertions.assertThat(actualUser.getAuthorities().size()).isEqualTo(1);
        Assertions.assertThat(actualUser.getAuthorities().toArray()).contains(new SimpleGrantedAuthority(actualUser.getRole().name()));

        Assertions.assertThat(actualUser.getUsername()).isEqualTo(actualUser.getEmail());
        Assertions.assertThat(actualUser.isAccountNonExpired()).isTrue();
        Assertions.assertThat(actualUser.isAccountNonLocked()).isTrue();
        Assertions.assertThat(actualUser.isCredentialsNonExpired()).isTrue();
        Assertions.assertThat(actualUser.isEnabled()).isTrue();
    }
}
