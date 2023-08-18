package coke.controller.camp.security;

import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.MemberRole;
import coke.controller.camp.repository.MemberRepository;
import coke.controller.camp.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest........");
        log.info(userRequest);

        log.info("oauth2 user..........................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((k,v) -> {
            log.info("-----------------paramMap-----------------------");
            log.info(k + ":" + v);
        });

        String email = null;
        String profileImg = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
            case "Google":
                email = oAuth2User.getAttribute("email");
                profileImg = oAuth2User.getAttribute("picture");
                break;
        }

        log.info("=====================email========================");
        log.info(email);
        log.info(profileImg);
        log.info("=============================================");

        return generateDTO(email, profileImg, paramMap);
    }

    private String getKakaoEmail(Map<String, Object> paraMap){

        log.info("Kakao.....................................");

        Object value = paraMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");

        log.info("email...." + email);

        return email;

    }

    public String getGoolgleEmail(Map<String, Object> paramMap){

        log.info("Google....................................");

        Object value = paramMap.get("email");

        log.info(value);

        String email = (String) value;

        return email;
    }


    private MemberSecurityDTO generateDTO(String email, String profileImg, Map<String, Object> param){

        log.info("*********************************************************");

        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isEmpty()){

            Member member = Member.builder()
                    .email(email)
                    .memberName(email)
                    .password(passwordEncoder.encode("1111"))
                    .fromSocial(true)
                    .profileImg(profileImg)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email, "1111", email, false, true, profileImg,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))  );
            memberSecurityDTO.setProps(param);

            return memberSecurityDTO;

        }else {
            Member member = result.get();

            if (member.getProfileImg() == null && profileImg != null){
                member.changeProfileImg(profileImg);
                memberRepository.save(member);
            }

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getEmail(),
                    member.getPassword(),
                    member.getMemberName(),
                    member.isDel(),
                    member.isFromSocial(),
                    member.getProfileImg(),
                    member.getRoleSet().stream().map(memberRole ->
                        new SimpleGrantedAuthority("ROLE_" + memberRole.name())
                    ).collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }




}
