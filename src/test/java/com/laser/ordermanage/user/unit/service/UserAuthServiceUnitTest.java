package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserAuthServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAuthService userAuthService;

    @Mock
    private UserEntityRepository userRepository;

    /**
     * 이메일 기준으로 사용자 객체 조회 성공
     */
    @Test
    public void getUserByEmailTest_성공() {
        // given
        final UserEntity user = UserEntity.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .role(Role.ROLE_CUSTOMER)
                .phone("01011111111")
                .zipCode("11111")
                .address("고객 주소")
                .detailAddress("고객 상세 주소")
                .build();

        // stub
        when(userRepository.findFirstByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        final UserEntity searchedUser = userAuthService.getUserByEmail(user.getEmail());

        // then
        Assertions.assertThat(searchedUser).isSameAs(user);
    }

    /**
     * 이메일 기준으로 사용자 객체 조회 실패
     * - 실패 사유 : 존재하지 않는 사용자
     */
    @Test
    public void getUserByEmailTest_실패_NOT_FOUND_ENTITY() {
        // given
        String invalidUserEmail = "invalid-user@gmail.com";

        // stub
        when(userRepository.findFirstByEmail(invalidUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.getUserByEmail(invalidUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("user" + ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }

}
