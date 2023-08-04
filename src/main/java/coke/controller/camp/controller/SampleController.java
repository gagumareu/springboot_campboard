package coke.controller.camp.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample/")
@Log4j2
public class SampleController {

    @GetMapping("/all")
    public void forAll(){
       log.info("exAll.....");
    }

    @GetMapping("/member")
    public void forMember(){
        log.info("exMember.....");
    }

    @GetMapping("/admin")
    public void forAdmin(){
        log.info("exAdmin.....");
    }

}
