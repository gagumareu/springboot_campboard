package coke.controller.camp.service;

import coke.controller.camp.dto.ReplyDTO;
import coke.controller.camp.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Transactional
    @Test
    public void getList(){

        Board board = Board.builder().bno(1616L).build();

        List<ReplyDTO> dtoList= replyService.getList(1616L);

        dtoList.forEach(replyDTO -> {
            System.out.println(Arrays.asList(replyDTO));
        });

    }
}
