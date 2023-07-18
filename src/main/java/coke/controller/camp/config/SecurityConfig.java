package coke.controller.camp.config;

import coke.controller.camp.entity.Member;
import coke.controller.camp.security.hadler.MemberLoginSuccessHandler;
import coke.controller.camp.security.service.MemberUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private MemberUserDetailService memberUserDetailService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests((auth) -> {
//            auth.antMatchers("/board/list").permitAll();
            auth.antMatchers("/member/myPage").hasRole("USER");
        });

        httpSecurity.formLogin();
        httpSecurity.csrf().disable();
        httpSecurity.logout();

        httpSecurity.oauth2Login().successHandler(successHandler());

        httpSecurity.rememberMe()
                .tokenValiditySeconds(60*60*24*7)
                .userDetailsService(memberUserDetailService);

        return httpSecurity.build();
    }

    @Bean
    public MemberLoginSuccessHandler successHandler(){
        return new MemberLoginSuccessHandler(passwordEncoder());
    }



}
