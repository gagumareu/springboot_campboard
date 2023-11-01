package coke.controller.camp.gearService;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

//    @Test
//    public void getBoardListSecondHands(){
//
//        List<BoardDTO> boardDTOList = boardService.getBoardBySecondHandsCategoryLimit();
//
//        boardDTOList.forEach(boardDTO -> {
//            System.out.println(boardDTO);
//        });
//
//    }
}
