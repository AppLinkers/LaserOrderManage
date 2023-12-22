package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetUserAccountResponse;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;

import java.util.List;

public interface UserEntityRepositoryCustom {

    List<GetUserEmailResponse> findEmailByNameAndPhone(String name, String phone);

    FactoryGetUserAccountResponse findUserAccountByFactory(String email);

    CustomerGetUserAccountResponse findUserAccountByCustomer(String email);
}
