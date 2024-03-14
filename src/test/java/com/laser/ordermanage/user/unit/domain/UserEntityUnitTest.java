package com.laser.ordermanage.user.unit.domain;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequestBuilder;
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
        final UpdateUserAccountRequest request = UpdateUserAccountRequestBuilder.build();

        // when
        actualUser.updateProperties(request);

        // then
        Assertions.assertThat(actualUser.getName()).isEqualTo(request.name());
        Assertions.assertThat(actualUser.getPhone()).isEqualTo(request.phone());
        Assertions.assertThat(actualUser.getAddress().getZipCode()).isEqualTo(request.zipCode());
        Assertions.assertThat(actualUser.getAddress().getAddress()).isEqualTo(request.address());
        Assertions.assertThat(actualUser.getAddress().getDetailAddress()).isEqualTo(request.detailAddress());
    }

    @Test
    public void extendsUserDetails() {
        // given
        final UserEntity actualUser = UserEntityBuilder.build();

        // then
        Assertions.assertThat(actualUser.getAuthorities().size()).isEqualTo(2);
        Assertions.assertThat(actualUser.getAuthorities().toArray()).contains(new SimpleGrantedAuthority(actualUser.getRole().name()));
        Assertions.assertThat(actualUser.getAuthorities().toArray()).contains(new SimpleGrantedAuthority(actualUser.getAuthority().name()));

        Assertions.assertThat(actualUser.getUsername()).isEqualTo(actualUser.getEmail());
        Assertions.assertThat(actualUser.isAccountNonExpired()).isTrue();
        Assertions.assertThat(actualUser.isAccountNonLocked()).isTrue();
        Assertions.assertThat(actualUser.isCredentialsNonExpired()).isTrue();
        Assertions.assertThat(actualUser.isEnabled()).isTrue();
    }
}
