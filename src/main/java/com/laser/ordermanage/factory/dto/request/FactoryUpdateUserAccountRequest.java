package com.laser.ordermanage.factory.dto.request;

import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FactoryUpdateUserAccountRequest {

    @Pattern(regexp = "^.{0,20}$", message = "상호의 최대 글자수는 10자입니다.")
    @NotBlank(message = "상호는 필수 입력값입니다.")
    private String companyName;

    @Pattern(regexp = "^.{0,10}$", message = "대표자 이름의 최대 글자수는 10자입니다.")
    @NotBlank(message = "대표자 이름은 필수 입력값입니다.")
    private String representative;

    @Valid
    private UpdateUserAccountRequest user;

    @NotEmpty(message = "FAX 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "FAX 번호 형식에 맞지 않습니다.")
    private String fax;


}
