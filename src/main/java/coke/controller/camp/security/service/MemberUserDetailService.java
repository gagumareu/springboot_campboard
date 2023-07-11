package coke.controller.camp.security.service;

import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.MemberRepository;
import coke.controller.camp.security.dto.MemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailService loadUserByUsername " + username);

        Optional<Member> result = memberRepository.findByEmail(username, false);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("check your email or social id");
        }

        Member member = result.get();

        log.info("------------------");
        log.info(member);

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream()
                        .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toSet())
        );

        memberAuthDTO.setName(member.getMemberName());
        memberAuthDTO.setFromSocial(member.isFromSocial());

        return memberAuthDTO;
    }
}
