package coke.controller.camp.controller;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.MemberJoinDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.security.dto.MemberSecurityDTO;
import coke.controller.camp.service.GearService;
import coke.controller.camp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final GearService gearService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public void myPage(){

        log.info("myPage...........");

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
        log.info("member modify....");

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myGear")
    public void myGear(Model model, Principal principal, PageRequestDTO pageRequestDTO){

        log.info("my gear....");

        String email = principal.getName();
        log.info(email);

        PageResultDTO<GearDTO, Object[]> gearList = gearService.getListWithPagination(email, pageRequestDTO);

        model.addAttribute("gearDTOList", gearList);
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

    @PreAuthorize("principal.username == #memberJoinDTO.email")
    @PostMapping("/modify")
    public String modify(MemberJoinDTO memberJoinDTO){

        log.info("memberSecurityDTO: " + memberJoinDTO);

        memberService.update(memberJoinDTO);

        return "redirect:/member/myPage";
    }


}
