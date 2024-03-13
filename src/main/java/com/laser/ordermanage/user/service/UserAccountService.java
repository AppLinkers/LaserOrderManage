package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.ChangePasswordToken;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.email.dto.EmailWithButtonRequest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.request.ChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.RequestChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import com.laser.ordermanage.user.dto.response.GetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final EmailService emailService;
    private final UserAuthService userAuthService;

    private final ChangePasswordTokenRedisRepository changePasswordTokenRedisRepository;

    private final UserEntityRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public ListResponse<GetUserEmailResponse> getUserEmail(String name, String phone) {
        return new ListResponse<>(userRepository.findEmailByNameAndPhone(name, phone));
    }

    @Transactional
    public void requestChangePassword(RequestChangePasswordRequest request) {
        UserEntity user = userAuthService.getUserByEmail(request.email());

        String changePasswordToken = jwtProvider.generateChangePasswordToken(user);

        changePasswordTokenRedisRepository.save(
                ChangePasswordToken.builder()
                        .email(user.getEmail())
                        .changePasswordToken(changePasswordToken)
                        .build()
        );

        String changePasswordLink = UriComponentsBuilder
                .fromHttpUrl(request.baseUrl())
                .queryParam("token", changePasswordToken)
                .toUriString();

        String subject = "[비밀번호 변경] 비밀번호 변경 링크를 보내드립니다.";
        String title = "비밀번호 변경";
        String content = "아래의 비밀번호 변경 버튼을 클릭하면 비밀번호를 재설정할 수 있습니다.";

        EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                .recipient(user.getEmail())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("비밀번호 변경하기")
                .buttonUrl(changePasswordLink)
                .build();
        emailService.sendEmailWithButton(emailWithButtonRequest);
    }

    @Transactional
    public void changePassword(HttpServletRequest httpServletRequest, ChangePasswordRequest request) {

        String resolvedToken = (String)httpServletRequest.getAttribute("resolvedToken");

        if (!jwtProvider.getType(resolvedToken).equals(JwtProvider.TYPE_CHANGE_PASSWORD)) {
            throw new CustomCommonException(UserErrorCode.INVALID_CHANGE_PASSWORD_TOKEN);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = userAuthService.getUserByEmail(authentication.getName());

        user.changePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public GetUserAccountResponse getUserAccount(String email) {
        return userRepository.findUserAccountByEmail(email);
    }

    @Transactional
    public void updateUserAccount(String email, UpdateUserAccountRequest request) {
        UserEntity user = userAuthService.getUserByEmail(email);

        user.updateProperties(request);
    }

    @Transactional
    public void changeEmailNotification(String email, Boolean isActivate) {
        UserEntity user = userAuthService.getUserByEmail(email);

        user.changeEmailNotification(isActivate);
    }
}
