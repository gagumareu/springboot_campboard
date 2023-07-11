package coke.controller.camp.security;

import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.MemberRole;
import coke.controller.camp.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@email.com")
                    .memberName("user" + i)
                    .password(passwordEncoder.encode("1111"))
                    .fromSocial(false)
                    .build();

            member.addMemberRole(MemberRole.USER);

            if (i > 80){
                member.addMemberRole(MemberRole.MANAGER);
            }

            if (i >90){
                member.addMemberRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @Test
    public void testRead(){

        Optional<Member> result = memberRepository.findByEmail("user95@email.com", false);

        Member member = result.get();

        System.out.println(member);
    }



}
