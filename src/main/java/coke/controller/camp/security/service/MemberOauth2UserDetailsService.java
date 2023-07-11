package coke.controller.camp.security.service;

import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.MemberRole;
import coke.controller.camp.repository.MemberRepository;
import coke.controller.camp.security.dto.MemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberOauth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("----------------------------");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("============================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);
        });

        String email = null;

        if (clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }

        log.info("email_Oauth2: " + email);

        Member member = saveSocialMember(email);

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())
                ).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        memberAuthDTO.setName(memberAuthDTO.getName());

        return memberAuthDTO;
    }

    private Member saveSocialMember(String email){

        Optional<Member> result = memberRepository.findByEmail(email, true);

        if (result.isPresent()){
            return result.get();
        }

        Member member = Member.builder()
                .email(email)
                .memberName(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        member.addMemberRole(MemberRole.USER);

        memberRepository.save(member);

        return member;

    }


}
