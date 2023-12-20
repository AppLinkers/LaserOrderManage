package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserEntityRepository userRepository;

    @Transactional(readOnly = true)
    public ListResponse<GetUserEmailResponse> getUserEmail(GetUserEmailRequest request) {
        return new ListResponse<>(userRepository.findEmailByNameAndPhone(request.getName(), request.getPhone()));
    }

}
