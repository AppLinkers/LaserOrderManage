package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.user.domain.type.Role;
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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private Role role;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Embedded
    private Address address;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "email_notification", nullable = false, length = 1)
    private Boolean emailNotification = Boolean.TRUE;

    @Builder
    public UserEntity(String email, String password, String name, Role role, String phone, Address address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> response = new ArrayList<>();
        response.add(new SimpleGrantedAuthority(role.name()));

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

    public void updateProperties(UpdateUserAccountRequest request) {
        this.name = request.name();
        this.phone = request.phone();
        this.address.updateProperties(request.zipCode(), request.address(), request.detailAddress());
    }
}
