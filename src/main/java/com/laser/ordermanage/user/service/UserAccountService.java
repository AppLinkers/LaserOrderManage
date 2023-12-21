package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.ChangePasswordToken;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.dto.request.RequestPasswordChangeRequest;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final MailService mailService;
    private final UserAuthService userAuthService;

    private final ChangePasswordTokenRedisRepository changePasswordTokenRedisRepository;

    private final UserEntityRepository userRepository;

    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public ListResponse<GetUserEmailResponse> getUserEmail(GetUserEmailRequest request) {
        return new ListResponse<>(userRepository.findEmailByNameAndPhone(request.getName(), request.getPhone()));
    }

    @Transactional
    public void requestPasswordChange(RequestPasswordChangeRequest request) {
        UserEntity user = userAuthService.getUserByEmail(request.getEmail());

        String changePasswordToken = jwtProvider.generateChangePasswordToken(user.getEmail());

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
}
