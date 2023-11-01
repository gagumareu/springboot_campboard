package coke.controller.camp.config;

import coke.controller.camp.security.CustomUserDetailsService;
import coke.controller.camp.security.handler.Custom403Handler;
import coke.controller.camp.security.handler.CustomLogoutSuccessHandler;
import coke.controller.camp.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("---------------configuration-------------------");


        http.formLogin(formLogin -> formLogin.loginPage("/member/login").successHandler(authenticationSuccessHandler()));

//        http.formLogin().loginPage("/member/login").successHandler(authenticationSuccessHandler());
        http.csrf(csrf -> csrf.disable());
        http.logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler()));

        http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

        http.oauth2Login(oauth2Login -> oauth2Login.loginPage("/member/login").successHandler(authenticationSuccessHandler()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("---------------web configure-------------");

        return (web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).requestMatchers("/static/**"));
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){

        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }




}
