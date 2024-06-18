package com.laser.ordermanage.user.dto.response;

import java.time.LocalDateTime;

public record KakaoAccountResponse(
        Long id,
        LocalDateTime connected_at,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
        Boolean name_needs_agreement,
        String name,
        Boolean has_email,
        Boolean email_needs_agreement,
        Boolean is_email_valid,
        Boolean is_email_verified,
        String email,
        Boolean has_phone_number,
        Boolean phone_number_needs_agreement,
        String phone_number
    ) { }
}
