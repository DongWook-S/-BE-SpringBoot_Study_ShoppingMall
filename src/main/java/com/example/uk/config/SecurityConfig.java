package com.example.uk.config;

import com.example.uk.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    /*
     * http 요청에 대한 보안 설정
     * 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메서드 등에 대한 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin()
            .loginPage("/members/login")    // 로그인 페이지 URL 설정
            .defaultSuccessUrl("/")         // 로그인 성공시 이동할 URL 설정
            .usernameParameter("email")     // 로그인시 사용할 파라미터 이름으로 email 지정
            .failureUrl("/members/login/error") // 로그인 실패시 이동할 URL 설정
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL 설정
            .logoutSuccessUrl("/")  // 로그아웃 성공시 이동할 URL 설정
        ;

        return http.build();
    }

    /*
     * BCryptPasswordEncoder 의 해시 함수를 이용하여 비밀번호를 암호화하여 저장
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

}
