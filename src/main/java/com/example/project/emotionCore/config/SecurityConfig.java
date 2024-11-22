package com.example.project.emotionCore.config;

import com.example.project.emotionCore.security.JwtAuthenticationFilter;
import com.example.project.emotionCore.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 모든 요청 허용 (특정 요청만 허용하려면 변경 가능)
                )
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API 서버의 경우 보통 비활성화)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable); // 폼 로그인 비활성화

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // REST API이므로 Basic Auth 및 CSRF 보안을 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                // JWT 사용으로 세션 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 특정 API는 모든 요청 허용
                        .requestMatchers("/members/sign-in").permitAll()
                        // USER 권한 필요
                        .requestMatchers("/members/test").hasRole("USER")
                        // 그 외 모든 요청 인증 필요
                        .anyRequest().authenticated()
                )
                // 커스텀 JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
