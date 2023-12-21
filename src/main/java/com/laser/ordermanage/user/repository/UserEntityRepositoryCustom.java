package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;

import java.util.List;

public interface UserEntityRepositoryCustom {

    List<GetUserEmailResponse> findEmailByNameAndPhone(String name, String phone);

}
