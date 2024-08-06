package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginKakaoRequest (
        @NotNull(message = "kakaoAccessToken 은 필수 입력값입니다.")
        String kakaoAccessToken
){ }
