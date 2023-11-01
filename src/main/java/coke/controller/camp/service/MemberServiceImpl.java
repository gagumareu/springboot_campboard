package coke.controller.camp.service;

import coke.controller.camp.dto.MemberDTO;
import coke.controller.camp.dto.MemberJoinDTO;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.MemberRole;
import coke.controller.camp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String email = memberJoinDTO.getEmail();

        boolean exist = memberRepository.existsById(email);

        if (exist){
            throw new MidExistException();
        }

        Member member = dtoToEntity(memberJoinDTO);

        member.changePassword(passwordEncoder.encode(memberJoinDTO.getPassword()));
        member.addRole(MemberRole.USER);

        log.info("===================================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);

    }

    @Override
    public void update(MemberJoinDTO memberJoinDTO) {

        Optional<Member> result = memberRepository.findById(memberJoinDTO.getEmail());

        Member member = result.get();

        if (memberJoinDTO.getPassword() != null){
            member.changeName(memberJoinDTO.getName());
            member.changePassword(passwordEncoder.encode(memberJoinDTO.getPassword()));
        }if(memberJoinDTO.getPassword() == null){
            member.changeName(memberJoinDTO.getName());
        }

        log.info("===================================");
        log.info(member);

        memberRepository.save(member);

    }

    @Override
    public MemberDTO getMemberByEmail(String email) {

        Optional<Member> result = memberRepository.findById(email);

        Member member = result.orElseThrow();

        return entityToDTO(member);
    }

    @Override
    public int checkIdForDuplication(String email) {

        return  memberRepository.checkID(email);
    }

}
