package coke.controller.camp.security.handler;

import coke.controller.camp.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess.............");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodePw = memberSecurityDTO.getPassword();

        if(memberSecurityDTO.isSocial() && (memberSecurityDTO.getPassword().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getPassword()))){

            log.info("Your Password must be changed");

            log.info("Redirect to member modify");
            response.sendRedirect("/member/modify");

            return;
        }else {
            response.sendRedirect("/board/list");
        }


    }
}
