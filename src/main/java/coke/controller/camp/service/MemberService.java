package coke.controller.camp.service;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.dto.MemberDTO;
import coke.controller.camp.dto.MemberJoinDTO;
import coke.controller.camp.entity.Member;
import coke.controller.camp.security.dto.MemberSecurityDTO;

import java.util.List;

public interface MemberService {

    static class MidExistException extends Exception{

    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    void update(MemberJoinDTO memberJoinDTO);
    MemberDTO getMemberByEmail(String email);
    int checkIdForDuplication(String email);

    default Member dtoToEntity(MemberJoinDTO memberJoinDTO){

        Member member = Member.builder()
                .email(memberJoinDTO.getEmail())
                .password(memberJoinDTO.getPassword())
                .memberName(memberJoinDTO.getName())
                .del(false)
                .fromSocial(false)
                .profileImg(memberJoinDTO.getProfileImg())
                .build();

        return member;
    }

    default MemberDTO entityToDTO(Member member){

        MemberDTO  memberDTO = MemberDTO.builder()
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .social(member.isFromSocial())
                .profileImg(member.getProfileImg())
                .build();
        return memberDTO;
    }


}
