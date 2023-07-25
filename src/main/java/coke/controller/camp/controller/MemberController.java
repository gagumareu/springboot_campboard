package coke.controller.camp.controller;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.service.GearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {


    @GetMapping("/myPage")
    public void myPage(){

        log.info("myPage...........");

        log.info("-----------------------------");

    }

    @GetMapping("/loginPage")
    public void loginPage(){

    }

    @GetMapping("/modify")
    public void modify(){

    }

    @GetMapping("/myGear")
    public void myGear(@ModelAttribute("email") String email){



    }





}
