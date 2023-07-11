package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insert(){

        IntStream.rangeClosed(706, 1606).forEach(i ->{

            int randomCounting = (int) ((Math.random()*5) +1);

            for (int j = 0; j < randomCounting; j++){

                Member member = Member.builder().email("user" + (int)((Math.random() *100) +1) + "@email.com").build();

                Reply reply = Reply.builder()
                        .text("reply text" + i)
                        .member(member)
                        .board(Board.builder().bno((long) i).build())
                        .build();
                replyRepository.save(reply);
            }
        });
    }

    @Transactional
    @Test
    public void remote(){

        replyRepository.deleteByBno(1641L);

    }



}
