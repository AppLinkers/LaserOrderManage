package com.laser.ordermanage.common.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@TestConfiguration
@RequiredArgsConstructor
public class TestWebSecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                        .requestMatchers("/secure/actuator/**").permitAll() // actuator 권한 허가
                        .requestMatchers("/user/logout", "/user/password", "/user/password/email-link").authenticated()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/customer/**", "/drawing/**").hasRole("CUSTOMER")
                        .requestMatchers("/factory/**").hasRole("FACTORY")
                        .anyRequest().authenticated()
        );

        http.exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler));
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

}
