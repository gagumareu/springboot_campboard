package coke.controller.camp.security;

import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.MemberRepository;
import coke.controller.camp.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Username: " + username);

        Optional<Member> result = memberRepository.getWithRole(username);

        if (result.isEmpty()){
            throw new UsernameNotFoundException("username not found......");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getEmail(),
                member.getPassword(),
                member.getMemberName(),
                false,
                false,
                member.getProfileImg(),
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                        .collect(Collectors.toList())
        );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }




}
