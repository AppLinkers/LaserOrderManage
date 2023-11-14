package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CreateCustomerDeliveryAddressRequest;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/customer/delivery-address")
@RestController
public class CustomerDeliveryAddressAPI {

    private final CustomerDeliveryAddressService customerDeliveryAddressService;

    @PostMapping("")
    public ResponseEntity<?> createCustomerDeliveryAddress(@RequestBody @Valid CreateCustomerDeliveryAddressRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerDeliveryAddressService.createCustomerDeliveryAddress(user, request);

        return ResponseEntity.ok().build();
    }
}
