package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserJoinService {

    private final MailService mailService;

    private final UserEntityRepository userRepository;
    private final VerifyCodeRedisRepository verifyCodeRedisRepository;

    public UserJoinStatusResponse requestEmailVerify(String email) {
        UserJoinStatusResponse response = checkDuplicatedEmail(email);

        if (response.getStatus().equals(JoinStatus.POSSIBLE.getCode())) {
            String title = "금오 M.T 회원가입 이메일 인증 번호";
            String verifyCode = createVerifyCode();
            mailService.sendEmail(email, title, verifyCode);
            // 이메일 인증번호 Redis 에 저장
            verifyCodeRedisRepository.save(
                    VerifyCode.builder()
                            .email(email)
                            .code(verifyCode)
                            .build()
            );
        }

        return response;

    }

    private String createVerifyCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomCommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public UserJoinStatusResponse verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        UserJoinStatusResponse response = checkDuplicatedEmail(verifyEmailRequest.getEmail());

        if (response.getStatus().equals(JoinStatus.POSSIBLE.getCode())) {
            VerifyCode verifyCode = verifyCodeRedisRepository.findById(verifyEmailRequest.getEmail())
                    .orElseThrow(() -> new CustomCommonException(ErrorCode.NOT_FOUND_VERIFY_CODE));

            if (!verifyCode.getCode().equals(verifyEmailRequest.getCode())) {
                throw new CustomCommonException(ErrorCode.INVALID_VERIFY_CODE);
            }
        }

        return response;
    }


    private UserJoinStatusResponse checkDuplicatedEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> UserJoinStatusResponse.builderWithUserEntity()
                        .userEntity(userEntity)
                        .status(JoinStatus.IMPOSSIBLE)
                        .buildWithUserEntity())
                .orElseGet(() -> UserJoinStatusResponse.builderWithOutUserEntity()
                        .status(JoinStatus.POSSIBLE)
                        .buildWithOutUserEntity());
    }
}
