package coke.controller.camp.controller;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.MemberJoinDTO;
import coke.controller.camp.security.dto.MemberSecurityDTO;
import coke.controller.camp.service.GearService;
import coke.controller.camp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myPage")
    public void myPage(){

        log.info("myPage...........");

        log.info("-----------------------------");

    }

    @GetMapping("/login")
    public void loginPage(String error, String logout){

        log.info("login get......");
        log.info("logout: " + logout);

        if(logout != null){
            log.info("user logout.....");
        }

    }

    @GetMapping("/modify")
    public void modify(){

    }

    @GetMapping("/myGear")
    public void myGear(@ModelAttribute("email") String email){


    }

    @GetMapping("/join")
    public void joinGET(){

        log.info("join get....");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){

        log.info("join post.....");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        }catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "email");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login";
    }

    @PostMapping("/modify")
    public String modify(MemberJoinDTO memberJoinDTO){

        log.info("memberSecurityDTO: " + memberJoinDTO);

        memberService.update(memberJoinDTO);

        return "redirect:/member/myPage";
    }


}
