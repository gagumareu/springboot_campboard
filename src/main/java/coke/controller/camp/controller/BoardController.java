package coke.controller.camp.controller;

import coke.controller.camp.dto.*;
import coke.controller.camp.service.*;
import coke.controller.camp.util.S3Uploader;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final PartyService partyService;

    private final PartyGearService partyGearService;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model){

        log.info("---------list-------");
        log.info(pageRequestDTO);

        PageResultDTO<BoardDTO, Object[]> result =  boardService.getListWithImageMemberAndReplyCnt(pageRequestDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication);

        model.addAttribute("boardList", result);
        model.addAttribute("authenticated", authentication);

    }

//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public void register(String category, Model model, Long gno){

        log.info("----------register method getMapping..........");
        log.info("category:  " + category);

        if(category.equals("secondHands") && gno != null){

            log.info("------------register for secondHands------------");

            GearDTO gearDTO = gearService.getByGno(gno);
            model.addAttribute("gearDTO", gearDTO);

        }

        model.addAttribute("tellCategory", category);

    }

    @PreAuthorize("principal.username == #boardDTO.email")
    @PostMapping("/register")
    public String register(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, GearDTO gearDTO, PartyDTO partyDTO ){

        log.info("-----------------register--------------------");
        log.info(boardDTO);
        log.info(gearDTO);
        log.info(partyDTO);

        // BindingResult error 가 있을 경우
        if (bindingResult.hasErrors()){

            log.info("has error to register a board............................");

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            if (boardDTO.getCategory().equals("secondHands") && gearDTO.getGno() != null){
                log.info("----- getting gearDTO");
                redirectAttributes.addAttribute("gno", gearDTO.getGno());
            }
            redirectAttributes.addAttribute("category", boardDTO.getCategory());

            return "redirect:/board/register";
        }

        Long bno = boardService.register(boardDTO);
        log.info(bno);

        // 중고글 작성시 기어 상태 변화
        if (gearDTO.getGno() != null && gearDTO.getState() == 1){
            log.info("----update gear to register second deal------ ");
            gearDTO.setBno(bno);
            gearService.updateState(gearDTO);
        }

        // 캠핑 모임 게시글 작성시
        if (partyDTO.getLocation() != null){
            log.info("-------- register for a party article------");
            partyDTO.setBno(bno);
            partyService.save(partyDTO);
        }

        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addAttribute("bno", bno);
        redirectAttributes.addAttribute("category", boardDTO.getCategory());

        return "redirect:/board/read";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "modify"})
    public void readOrModify(Model model, @RequestParam("bno") Long bno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Authentication authentication){

        log.info("---------read or modify-------");
        log.info(pageRequestDTO);
        log.info(bno);
        log.info(authentication.getName());
        log.info(authentication.getPrincipal());

        BoardDTO boardDTO = boardService.getBoardWithImagesMemberAndReplies(bno);

        if (pageRequestDTO.getCategory() == null || pageRequestDTO.getCategory() == ""){
            pageRequestDTO.setCategory(boardDTO.getCategory());
        }

        log.info(boardDTO);
        log.info("category: " + pageRequestDTO.getCategory());
        log.info("boardDTO.getCategory: " + boardDTO.getCategory());

        if (pageRequestDTO.getCategory().equals("party") || boardDTO.getCategory().equals("party")){

            log.info("-----------get party location------------");
            PartyDTO partyDTO = partyService.getPartyInfoByBno(bno);
            model.addAttribute("partyDTO", partyDTO);
        }

        model.addAttribute("dto", boardDTO);
        model.addAttribute("tellingCategory", boardDTO.getCategory());
    }

    @PreAuthorize("principal.username == #boardDTO.email")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){

        log.info("---------remove-------");
        log.info("boardDTO: " + boardDTO);
        log.info(pageRequestDTO);

        List<BoardImageDTO> boardImageList = boardImageService.getImageList(boardDTO.getBno());
        log.info("boardImageList: " + boardImageList);

        if (boardImageList != null && boardImageList.size() > 0){
            boardImageList.forEach(boardImageDTO -> {
                String fileName = boardImageDTO.getUuid() + "_" + boardImageDTO.getFileName();
                s3Uploader.removeS3File(fileName);
            });
        }

        if (boardDTO.getCategory().equals("party")){
            partyService.removePartiesByBno(boardDTO.getBno());
            int result = partyGearService.deleteAllByBno(boardDTO.getBno());
            log.info(result);
        }

        gearService.backStateZero(boardDTO.getBno());

        boardService.remove(boardDTO.getBno());

        redirectAttributes.addAttribute("category", pageRequestDTO.getCategory());

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
    public String modify(@Valid BoardDTO boardDTO, BindingResult bindingResult, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes, PartyDTO partyDTO){

        log.info("----------modify---------");
        log.info(boardDTO);
        log.info(partyDTO);

        if (bindingResult.hasErrors()){
            log.info("has errors.....");
            log.info(boardDTO.getBno());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());
//            redirectAttributes.addAttribute("pageRequestDTO", pageRequestDTO);

            return "redirect:/board/modify";
        }

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("category", pageRequestDTO.getCategory());

        return "redirect:/board/read";
    }

    @GetMapping(value = "/list/talkCategory")
    public ResponseEntity<List<BoardDTO>> getListByTalkyCategory(){

        log.info("------getListByPartyCategory------");

        List<BoardDTO> boardDTOList = boardService.getBoardByTalkCategoryLimit();

        return new ResponseEntity<>(boardDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/list/secondHands")
    public ResponseEntity<List<BoardDTO>> getListBySecondHandsCategory(){

        log.info("------getListBySecondHandsCategory------");

        List<BoardDTO> boardDTOList = boardService.getBoardBySecondHandsCategoryLimit();

        return new ResponseEntity<>(boardDTOList, HttpStatus.OK);
    }





}
