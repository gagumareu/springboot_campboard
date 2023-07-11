package coke.controller.camp.security;

import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.MemberRole;
import coke.controller.camp.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class PasswordTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testsEncode(){

        String password = "1234";

        String encodePassword = passwordEncoder.encode(password);

        System.out.println("encodePassword: " + encodePassword);;

        boolean matchResult = passwordEncoder.matches(password, encodePassword);

        System.out.println("matchResult: " + matchResult);


    }
}
