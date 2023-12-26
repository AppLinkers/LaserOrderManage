package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.ChangePasswordToken;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.request.ChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.RequestPasswordChangeRequest;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final MailService mailService;
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
    public void requestPasswordChange(RequestPasswordChangeRequest request) {
        UserEntity user = userAuthService.getUserByEmail(request.getEmail());

        String changePasswordToken = jwtProvider.generateChangePasswordToken(user);

        changePasswordTokenRedisRepository.save(
                ChangePasswordToken.builder()
                        .email(user.getEmail())
                        .changePasswordToken(changePasswordToken)
                        .build()
        );

        String changePasswordLink = UriComponentsBuilder
                .fromHttpUrl(request.getBaseUrl())
                .queryParam("token", changePasswordToken)
                .toUriString();

        String title = "비밀번호 변경 링크를 보내드립니다.";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("비밀번호 변경 링크 : ")
                .append(changePasswordLink);
        String content = sbContent.toString();

        mailService.sendEmail(user.getEmail(), title, content);
    }

    @Transactional
    public void changePassword(HttpServletRequest httpServletRequest, ChangePasswordRequest request) {

        String resolvedToken = (String)httpServletRequest.getAttribute("resolvedToken");

        if (!StringUtils.hasText(resolvedToken) || !jwtProvider.getType(resolvedToken).equals(JwtProvider.TYPE_CHANGE_PASSWORD)) {
            throw new CustomCommonException(ErrorCode.INVALID_CHANGE_PASSWORD_JWT_TOKEN);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = userAuthService.getUserByEmail(authentication.getName());

        user.changePassword(passwordEncoder.encode(request.getPassword()));
    }

    @Transactional
    public void changeEmailNotification(String email, Boolean isActivate) {
        UserEntity user = userAuthService.getUserByEmail(email);

        user.changeEmailNotification(isActivate);
    }
}
