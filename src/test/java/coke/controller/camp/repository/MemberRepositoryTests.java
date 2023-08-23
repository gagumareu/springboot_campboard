package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insert(){
        IntStream.rangeClosed(1, 3).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@email.com")
                    .memberName("user" + i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void getMemberByEmail(){

        Member memberEmail = Member.builder().email("udekang2@naver.com").build();

        Optional<Member> result = memberRepository.findById("udekang2@naver.com");

        Member member = result.orElseThrow();

        System.out.println(member);

    }

}
