package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.mail.dto.MailRequest;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.JoinCustomerRequest;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserJoinService {

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final UserEntityRepository userRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final VerifyCodeRedisRepository verifyCodeRedisRepository;

    @Transactional
    public UserJoinStatusResponse requestEmailVerify(String email) {
        UserJoinStatusResponse response = checkDuplicatedEmail(email);

        if (JoinStatus.isPossible(response.status())) {
            String subject = "[이메일 인증] 회원가입 이메일 인증 번호";
            String title = "이메일 인증";

            StringBuilder sbContent = new StringBuilder();
            sbContent.append("가입 화면에서 아래 인증번호를 입력해주세요.<br/>");
            String verifyCode = createVerifyCode();
            sbContent.append(verifyCode);
            String content = sbContent.toString();

            MailRequest mailRequest = MailRequest.builder()
                    .toEmail(email)
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("금오 레이저")
                    .buttonUrl("https://www.kumoh.org/")
                    .build();
            mailService.sendEmail(mailRequest);

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

    @Transactional
    public UserJoinStatusResponse verifyEmail(VerifyEmailRequest request) {
        UserJoinStatusResponse response = checkDuplicatedEmail(request.email());

        if (JoinStatus.isPossible(response.status())) {
            VerifyCode verifyCode = verifyCodeRedisRepository.findById(request.email())
                    .orElseThrow(() -> new CustomCommonException(UserErrorCode.NOT_FOUND_VERIFY_CODE));

            if (!verifyCode.getCode().equals(request.code())) {
                throw new CustomCommonException(UserErrorCode.INVALID_VERIFY_CODE);
            }

            // 인증 완료 후, 인증 코드 삭제
            verifyCodeRedisRepository.deleteById(request.email());
        }

        return response;
    }

    @Transactional
    public UserJoinStatusResponse joinCustomer(JoinCustomerRequest request) {
        UserJoinStatusResponse response = checkDuplicatedEmail(request.email());

        if (JoinStatus.isPossible(response.status())) {
            UserEntity user = UserEntity.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.ROLE_CUSTOMER)
                    .phone(request.phone())
                    .zipCode(request.zipCode())
                    .address(request.address())
                    .detailAddress(request.detailAddress())
                    .build();

            Customer customer = Customer.builder()
                    .user(user)
                    .name(request.name())
                    .companyName(request.companyName())
                    .build();

            DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                    .customer(customer)
                    .name(customer.getName())
                    .zipCode(user.getZipCode())
                    .address(user.getAddress())
                    .detailAddress(user.getDetailAddress())
                    .receiver(customer.getName())
                    .phone1(user.getPhone())
                    .phone2(user.getPhone())
                    .isDefault(Boolean.TRUE)
                    .build();

            deliveryAddressRepository.save(deliveryAddress);

            response = UserJoinStatusResponse.builderWithUserEntity()
                    .userEntity(user)
                    .status(JoinStatus.COMPLETED)
                    .buildWithUserEntity();
        }

        return response;
    }


    private UserJoinStatusResponse checkDuplicatedEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .map(userEntity -> UserJoinStatusResponse.builderWithUserEntity()
                        .userEntity(userEntity)
                        .status(JoinStatus.IMPOSSIBLE)
                        .buildWithUserEntity())
                .orElseGet(() -> UserJoinStatusResponse.builderWithOutUserEntity()
                        .status(JoinStatus.POSSIBLE)
                        .buildWithOutUserEntity());
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
            throw new CustomCommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
