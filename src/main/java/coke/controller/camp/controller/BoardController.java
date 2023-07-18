package coke.controller.camp.controller;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.repository.BoardImageRepository;
import coke.controller.camp.security.dto.MemberAuthDTO;
import coke.controller.camp.service.BoardImageService;
import coke.controller.camp.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardImageService boardImageService;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal MemberAuthDTO memberAuthDTO){

        log.info("---------list-------");
        log.info(memberAuthDTO);

        PageResultDTO<BoardDTO, Object[]> result =  boardService.getListWithImageMemberAndReplyCnt(pageRequestDTO);

        model.addAttribute("boardList", result);

    }

    @GetMapping("/register")
    public void register(){
    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes, Model model){

        log.info("---------register-------");

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addAttribute("bno", bno);

        return "redirect:/board/read";
    }

    @GetMapping({"/read", "modify"})
    public void readOrModify(Model model, @RequestParam("bno") Long bno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO){

        log.info("---------read or modify-------");

        log.info(bno);

        BoardDTO boardDTO = boardService.getBoardWithImagesMemberAndReplies(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/remove")
    public String remove(Long bno){

        log.info("---------remove-------");
        log.info("bno: " + bno);

        List<BoardImageDTO> boardImageList = boardImageService.getImageList(bno);
        log.info("boardImageList: " + boardImageList);

        if (boardImageList != null && boardImageList.size() > 0){
            deleteFiles(boardImageList);
        }

        boardService.remove(bno);

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

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("----------modify---------");
        log.info(boardDTO);

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());

        return "redirect:/board/read";
    }




}
