package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CustomerCreateOrUpdateDeliveryAddressRequest (

    @NotEmpty(message = "배송지 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "배송지 이름의 최대 글자수는 20자입니다.")
    String name,

    @NotNull(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    String zipCode,

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    String address,

    @Pattern(regexp = "^.{0,30}$", message = "상세 주소의 최대 글자수는 30자입니다.")
    String detailAddress,

    @NotEmpty(message = "수신자는 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "수신자의 최대 글자수는 10자입니다.")
    String receiver,

    @NotNull(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone1,

    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone2,

    @NotNull(message = "배송지 기본 여부는 필수 사항입니다.")
    Boolean isDefault

) {
    public DeliveryAddress toEntity(Customer customer) {
        Address addressEntity = Address.builder()
                .zipCode(zipCode)
                .address(address)
                .detailAddress(detailAddress)
                .build();

        return DeliveryAddress.builder()
                .customer(customer)
                .name(name)
                .address(addressEntity)
                .receiver(receiver)
                .phone1(phone1)
                .phone2(phone2)
                .isDefault(isDefault)
                .build();
    }
}
