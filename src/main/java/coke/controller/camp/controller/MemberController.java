package coke.controller.camp.controller;

import coke.controller.camp.dto.*;
import coke.controller.camp.service.BoardService;
import coke.controller.camp.service.GearService;
import coke.controller.camp.service.MemberService;
import coke.controller.camp.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final GearService gearService;
    private final BoardService boardService;
    private final ReplyService replyService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public void myPage(Model model, Principal principal){

        log.info("myPage...........");

        String email = principal.getName();
        log.info(email);

        MemberDTO memberDTO = memberService.getMemberByEmail(email);
        log.info(memberDTO);

        model.addAttribute("dto", memberDTO);

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

    @GetMapping(value = "/board/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardDTO>> getBoardListByEmail(@PathVariable("email") String email){

        log.info("---------getting boardList By email-------------");
        log.info(email);

        return new ResponseEntity<>(boardService.getBoardByEmail(email), HttpStatus.OK  );
    }

    @GetMapping(value = "/replies/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getRepliesByEmail(@PathVariable("email") String email){

        log.info("------------getting replies by email----------");
        log.info(email);

        return new ResponseEntity<>(replyService.getListByEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/checkingId/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> checkIdForSignIn(@PathVariable ("email") String email){

        log.info("checking email for sign in : " + email);

        return new ResponseEntity<>(memberService.checkIdForDuplication(email), HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myGear")
    public void myGear(Model model, Principal principal, PageRequestDTO pageRequestDTO){

        log.info("-----myGear--------");
        log.info(pageRequestDTO);
        String email = principal.getName();
        log.info(email);

        PageResultDTO<GearDTO, Object[]> gearList = gearService.getListWithPagination(email, pageRequestDTO);

        model.addAttribute("gearList", gearList);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mySchedule")
    public void mySchedule(Principal principal){

        log.info(principal.getName());

    }

}
