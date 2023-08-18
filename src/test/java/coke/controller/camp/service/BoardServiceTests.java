package coke.controller.camp.service;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Transactional
    @Test
    public void insert(){
        IntStream.rangeClosed(1, 300).forEach(i ->{

            Member member = Member.builder().email("user" + ((Math.random() *99) +0) + "@email.com").build();

            BoardDTO boardDTO = BoardDTO.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .email(member.getEmail())
                    .build();

            List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

            int randomCount = (int) ((Math.random() *5) +0);

            for (int j = 0; j < randomCount; j++){
                BoardImageDTO boardImageDTO = BoardImageDTO.builder()
                        .folderPath("folderPath_temp")
                        .uuid(UUID.randomUUID().toString())
                        .fileName("fileName" + j)
                        .build();
                boardImageDTOList.add(boardImageDTO);
            }
            boardDTO.setBoardImageDTOList(boardImageDTOList);

            System.out.println("---------------------------------");
            System.out.println("boardDTO: " + boardDTO);
            System.out.println("boardImageDTOList: " + boardImageDTOList);

            boardService.register(boardDTO);

        });
    }

    @Test
    public void remove(){

        boardService.remove(601L);
    }

    @Test
    public void getListBoardByEmail(){

        List<BoardDTO> dtoList = boardService.getBoardByEmail("udekang2@naver.com");

        for (BoardDTO dto : dtoList){
            System.out.println(Arrays.asList(dto));
        }

    }

}
