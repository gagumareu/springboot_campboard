package coke.controller.camp.controller;

import coke.controller.camp.dto.ReplyDTO;
import coke.controller.camp.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {

        log.info("------register a reply---------------");
        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("bno") Long bno){

        log.info("------getList_replies-----");

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);

    }

    @GetMapping(value = "/{rno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplyDTO> get(@PathVariable("rno") Long rno){

        ReplyDTO replyDTO = replyService.get(rno);

        return new ResponseEntity<>(replyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<String> delete(@PathVariable("rno") Long rno){

        log.info("---------delete a reply -----------");

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){

        log.info("----------modify a reply -------------");
        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


}
