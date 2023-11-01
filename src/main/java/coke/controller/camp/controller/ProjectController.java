package coke.controller.camp.controller;

import coke.controller.camp.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Log4j2
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final PartyService partyService;

    @GetMapping("/intro")
    public void intro(){

    }

    @GetMapping("/about")
    public void about(){

    }

    @GetMapping("/calendar")
    public void calendar(){

    }

    @GetMapping("/map")
    public void map(Model model){

        log.info("****** camping location ************");
        String location = partyService.getLocationByBno(156L);
        model.addAttribute("campingLocation", location);
    }

    @GetMapping("/map2")
    public void map2(Model model){



    }

}
