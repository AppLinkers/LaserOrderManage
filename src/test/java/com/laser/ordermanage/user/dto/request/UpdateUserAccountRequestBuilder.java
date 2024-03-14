package com.laser.ordermanage.user.dto.request;

public class UpdateUserAccountRequestBuilder {

    public static UpdateUserAccountRequest build() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest nullNameBuild() {
        return UpdateUserAccountRequest.builder()
                .name(null)
                .phone("01012341234")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest emptyNameBuild() {
        return UpdateUserAccountRequest.builder()
                .name("")
                .phone("01012341234")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest invalidNameBuild() {
        return UpdateUserAccountRequest.builder()
                .name("너어어어무우우우긴이름")
                .phone("01012341234")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest nullPhoneBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone(null)
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest invalidPhoneBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("invalid-phone")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest nullZipCodeBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode(null)
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest invalidZipCodeBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode("invalid-zip-code")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest nullAddressBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode("98765")
                .address(null)
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest emptyAddressBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode("98765")
                .address("")
                .detailAddress("수정 상세 주소")
                .build();
    }

    public static UpdateUserAccountRequest invalidDetailAddressBuild() {
        return UpdateUserAccountRequest.builder()
                .name("수정 이름")
                .phone("01012341234")
                .zipCode("98765")
                .address("수정 기본 주소")
                .detailAddress("너어어어어어어어어어어어어무우우우우우우우우우우우우긴상세주소")
                .build();
    }
}
