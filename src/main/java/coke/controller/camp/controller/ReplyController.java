package coke.controller.camp.controller;

import coke.controller.camp.dto.ReplyDTO;
import coke.controller.camp.entity.Reply;
import coke.controller.camp.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
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

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


}
