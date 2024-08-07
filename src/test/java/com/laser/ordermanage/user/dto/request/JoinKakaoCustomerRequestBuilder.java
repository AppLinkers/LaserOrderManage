package com.laser.ordermanage.user.dto.request;

import com.laser.ordermanage.customer.dto.request.JoinKakaoCustomerRequest;

public class JoinKakaoCustomerRequestBuilder {

    public static JoinKakaoCustomerRequest build() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest duplicateEmailBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest nullEmailBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email(null)
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidEmailBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email(".user.name@domain.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest nullNameBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name(null)
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest emptyNameBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidNameBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("너어어어무우우우긴이름")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidCompanyNameBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("너어어어어어어어무우우우우우우우긴회사이름")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest nullPhoneBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone(null)
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidPhoneBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("invalid-phone")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest nullZipCodeBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode(null)
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidZipCodeBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("invalid-zip-code")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest nullAddressBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address(null)
                .build();
    }

    public static JoinKakaoCustomerRequest emptyAddressBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("")
                .build();
    }

    public static JoinKakaoCustomerRequest invalidDetailAddressBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .detailAddress("너어어어어어어어어어어어어무우우우우우우우우우우우우긴상세주소")
                .build();
    }
}
