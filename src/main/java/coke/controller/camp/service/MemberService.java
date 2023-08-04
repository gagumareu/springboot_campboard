package coke.controller.camp.service;

import coke.controller.camp.dto.MemberJoinDTO;
import coke.controller.camp.entity.Member;
import coke.controller.camp.security.dto.MemberSecurityDTO;

public interface MemberService {

    static class MidExistException extends Exception{

    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    void update(MemberJoinDTO memberJoinDTO);

    default Member dtoToEntity(MemberJoinDTO memberJoinDTO){

        Member member = Member.builder()
                .email(memberJoinDTO.getEmail())
                .password(memberJoinDTO.getPassword())
                .memberName(memberJoinDTO.getName())
                .del(false)
                .fromSocial(false)
                .build();

        return member;
    }

}
