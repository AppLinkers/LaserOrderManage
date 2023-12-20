package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final MailService mailService;
    private final UserAuthService userAuthService;

    private final UserEntityRepository userRepository;
    private final VerifyCodeRedisRepository verifyCodeRedisRepository;

    @Transactional(readOnly = true)
    public ListResponse<GetUserEmailResponse> getUserEmail(GetUserEmailRequest request) {
        return new ListResponse<>(userRepository.findEmailByNameAndPhone(request.getName(), request.getPhone()));
    }

    @Transactional
    public void requestEmailVerifyForGetPassword(String email) {

        UserEntity user = userAuthService.getUserByEmail(email);

        String title = "금오 M.T 비밀번호 찾기 이메일 인증 번호";
        String verifyCode = mailService.createVerifyCode();
        mailService.sendEmail(user.getEmail(), title, verifyCode);
        // 이메일 인증번호 Redis 에 저장
        verifyCodeRedisRepository.save(
                VerifyCode.builder()
                        .email(user.getEmail())
                        .code(verifyCode)
                        .build()
        );
    }
}
