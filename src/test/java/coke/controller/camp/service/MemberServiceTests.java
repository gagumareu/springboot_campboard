package coke.controller.camp.service;

import coke.controller.camp.dto.MemberDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Test
    public void get(){

        MemberDTO memberDTO = memberService.getMemberByEmail("udekang2@naver.com");

        System.out.println(memberDTO);
    }
}
