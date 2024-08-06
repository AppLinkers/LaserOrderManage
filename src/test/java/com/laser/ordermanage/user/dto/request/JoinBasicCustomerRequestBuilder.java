package com.laser.ordermanage.user.dto.request;

import com.laser.ordermanage.customer.dto.request.JoinBasicCustomerRequest;

public class JoinBasicCustomerRequestBuilder {

    public static JoinBasicCustomerRequest build() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest duplicateEmailBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullEmailBuild() {
        return JoinBasicCustomerRequest.builder()
                .email(null)
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidEmailBuild() {
        return JoinBasicCustomerRequest.builder()
                .email(".user.name@domain.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullPasswordBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password(null)
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidPasswordBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("invalid-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullNameBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name(null)
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest emptyNameBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidNameBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("너어어어무우우우긴이름")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidCompanyNameBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("너어어어어어어어무우우우우우우우긴회사이름")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullPhoneBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone(null)
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidPhoneBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("invalid-phone")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullZipCodeBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode(null)
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest invalidZipCodeBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("invalid-zip-code")
                .address("address")
                .build();
    }

    public static JoinBasicCustomerRequest nullAddressBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address(null)
                .build();
    }

    public static JoinBasicCustomerRequest emptyAddressBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("")
                .build();
    }

    public static JoinBasicCustomerRequest invalidDetailAddressBuild() {
        return JoinBasicCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .detailAddress("너어어어어어어어어어어어어무우우우우우우우우우우우우긴상세주소")
                .build();
    }

}
