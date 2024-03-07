package com.laser.ordermanage.user.dto.request;

import com.laser.ordermanage.customer.dto.request.JoinCustomerRequest;

public class JoinCustomerRequestBuilder {

    public static JoinCustomerRequest build() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest duplicateEmailBuild() {
        return JoinCustomerRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .name("new-user")
                .companyName("company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullEmailBuild() {
        return JoinCustomerRequest.builder()
                .email(null)
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidEmailBuild() {
        return JoinCustomerRequest.builder()
                .email(".user.name@domain.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullPasswordBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password(null)
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidPasswordBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("invalid-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullNameBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name(null)
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest emptyNameBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidNameBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("너어어어무우우우긴이름")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidCompanyNameBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("너어어어어어어어무우우우우우우우긴회사이름")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullPhoneBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone(null)
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidPhoneBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("invalid-phone")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullZipCodeBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode(null)
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest invalidZipCodeBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("invalid-zip-code")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest nullAddressBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address(null)
                .build();
    }

    public static JoinCustomerRequest emptyAddressBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("")
                .build();
    }

    public static JoinCustomerRequest invalidDetailAddressBuild() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .detailAddress("너어어어어어어어어어어어어무우우우우우우우우우우우우긴상세주소")
                .build();
    }

}
