package com.laser.ordermanage.user.dto.response;

import java.util.List;

public class GetUserEmailResponseBuilder {

    public static List<GetUserEmailResponse> buildListForCustomer() {
        GetUserEmailResponse user1EmailResponse = GetUserEmailResponse.builder()
                .email("user1@gmail.com")
                .name("고객 이름 1")
                .build();

        GetUserEmailResponse user1CopyEmailResponse = GetUserEmailResponse.builder()
                .email("user1-copy@gmail.com")
                .name("고객 이름 1")
                .build();

        return List.of(user1EmailResponse, user1CopyEmailResponse);
    }

    public static List<GetUserEmailResponse> buildListForFactory() {
        GetUserEmailResponse factoryEmailResponse = GetUserEmailResponse.builder()
                .email("admin@kumoh.org")
                .name("관리자")
                .build();

        return List.of(factoryEmailResponse);
    }
}
