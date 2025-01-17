package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.domain.type.SignupMethod;
import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "user_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends CreatedAtEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Embedded
    private Address address;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "email_notification", nullable = false, length = 1)
    private Boolean emailNotification = Boolean.TRUE;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "signup_method", nullable = false, updatable = false)
    private SignupMethod signupMethod;

    @Builder
    public UserEntity(String email, String password, String name, Role role, Authority authority, String phone, Address address, SignupMethod signupMethod) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.signupMethod = signupMethod;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> response = new ArrayList<>();
        response.add(new SimpleGrantedAuthority(role.name()));
        if (authority != null) {
            response.add(new SimpleGrantedAuthority(authority.name()));
        }

        return response;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public boolean isSocialAccount() {
        return !this.signupMethod.equals(SignupMethod.BASIC);
    }

    public void updateProperties(UpdateUserAccountRequest request) {
        this.name = request.name();
        this.phone = request.phone();
        this.address.updateProperties(request.zipCode(), request.address(), request.detailAddress());
    }
}
