package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Test
    public void insert(){

        IntStream.rangeClosed(1, 300).forEach(i -> {

            Member member = Member.builder().email("user" +(int)((Math.random() * 99) +1) + "@email.com").build();

            Board board = Board.builder()
                    .title("title " + i)
                    .content("board content" + i)
                    .member(member).build();

            boardRepository.save(board);

            int count = (int) ((Math.random() * 5) + 0);

            for (int j = 0; j < count; j++){

                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .uuid(UUID.randomUUID().toString())
                        .fileName("board image file (board no: " + i + ", image no: " + j + ".jpg")
                        .build();

                boardImageRepository.save(boardImage);
            }
        });

    }

    @Test
    public void search(){
        boardRepository.search();
    }

    @Test
    public void insertCategory(){

        IntStream.rangeClosed(627, 999).forEach(value -> {

        });

    }




}
