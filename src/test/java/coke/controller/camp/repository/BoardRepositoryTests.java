package coke.controller.camp.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

//    @Test
//    public void getListWithTalkCategory(){
//
//        List<Object[]> boardList =boardRepository.getBoardListByCategoryTalkLimit();
//
//        boardList.forEach(board -> {
//            System.out.println(Arrays.toString(board));
//        });
//
//    }
//
//    @Test
//    public void getListWithSecondHansCategory(){
//
//        List<Object[]> boardList =boardRepository.getBoardListByCategorySecondHansLimit();
//
//        boardList.forEach(board -> {
//            System.out.println(Arrays.toString(board));
//        });
//
//    }





}
