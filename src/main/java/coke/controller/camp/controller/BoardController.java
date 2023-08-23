package coke.controller.camp.controller;

import coke.controller.camp.dto.*;
import coke.controller.camp.service.BoardImageService;
import coke.controller.camp.service.BoardService;
import coke.controller.camp.service.GearService;
import coke.controller.camp.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.File;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardController {

    private final BoardService boardService;

    private final BoardImageService boardImageService;

    private final GearService gearService;

    private final S3Uploader s3Uploader;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model){

        log.info("---------list-------");

        PageResultDTO<BoardDTO, Object[]> result =  boardService.getListWithImageMemberAndReplyCnt(pageRequestDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication);

        model.addAttribute("boardList", result);
        model.addAttribute("authenticated", authentication);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public String register(String category, Model model, Principal principal, Long gno){

        log.info("----------register method getMapping..........");

        if(category.equals("secondHands")){

            log.info("------------register for secondHands------------");

//            log.info(principal.getName());

//            List<GearDTO> gearList = gearService.getList(principal.getName());

//            log.info(gearList);

            if (gno != null){
                GearDTO gearDTO = gearService.getByGno(gno);
                model.addAttribute("gearDTO", gearDTO);
            }

//            model.addAttribute("gearList", gearList);
            model.addAttribute("tellCategory", category);
//            model.addAttribute("principalName", principal.getName());

        }

        return "/board/register";
    }

    @PreAuthorize("principal.username == #boardDTO.email")
    @PostMapping("/register")
    public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes, GearDTO gearDTO){

        log.info("---------register-------");
        log.info(boardDTO);
        log.info(gearDTO);

        Long bno = boardService.register(boardDTO);
        if (gearDTO.getGno() != null && gearDTO.getState() == 1){
            log.info("----update gear to register second deal------ ");
            gearService.updateState(gearDTO);
        }

        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addAttribute("bno", bno);
        redirectAttributes.addAttribute("category", boardDTO.getCategory());

        return "redirect:/board/read";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "modify"})
    public void readOrModify(Model model, @RequestParam("bno") Long bno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO){

        log.info("---------read or modify-------");

        log.info(bno);

        BoardDTO boardDTO = boardService.getBoardWithImagesMemberAndReplies(bno);

        log.info(boardDTO);
        log.info("category: " + pageRequestDTO.getCategory());

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("principal.username == #boardDTO.email")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO){

        log.info("---------remove-------");
        log.info("boardDTO: " + boardDTO);

        List<BoardImageDTO> boardImageList = boardImageService.getImageList(boardDTO.getBno());
        log.info("boardImageList: " + boardImageList);

        if (boardImageList != null && boardImageList.size() > 0){
//            deleteFiles(boardImageList);
            boardImageList.forEach(boardImageDTO -> {
                String fileName = boardImageDTO.getUuid() + "_" + boardImageDTO.getFileName();
                s3Uploader.removeS3File(fileName);
            });
        }

        boardService.remove(boardDTO.getBno());

        return "redirect:/board/list";

    }

    public void deleteFiles(List<BoardImageDTO> boardImageList){

        log.info("------------deleteFiles-----------");

        boardImageList.forEach(boardImage -> {

            String folderPath = boardImage.getFolderPath();
            String uuid = boardImage.getUuid();
            String fileName = boardImage.getFileName();

            File file = new File(uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName);
            log.info("file: " + file);
            file.delete();

            File thumbnail = new File(file.getParent() + File.separator + "s_" + file.getName());
            log.info("thumbnail: " + thumbnail);
            thumbnail.delete();

        });
    }

    @PreAuthorize("principal.username == #boardDTO.email")
    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("----------modify---------");
        log.info(boardDTO);

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("category", pageRequestDTO.getCategory());

        return "redirect:/board/read";
    }




}
