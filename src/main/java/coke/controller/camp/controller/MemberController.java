package coke.controller.camp.controller;

import coke.controller.camp.security.dto.MemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public void myPage(@AuthenticationPrincipal MemberAuthDTO memberAuthDTO){

        log.info("myPage...........");

        log.info("-----------------------------");
        log.info(memberAuthDTO);

    }

    @GetMapping("/loginPage")
    public void loginPage(){

    }



}
