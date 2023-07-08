package coke.controller.camp.service;

import coke.controller.camp.dto.ReplyDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);
    List<ReplyDTO> getList(Long bno);
    ReplyDTO get(Long rno);
    void remove(Long rno);
    void modify(ReplyDTO replyDTO);

    default Reply dtoToEntity(ReplyDTO replyDTO){

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .member(Member.builder().email(replyDTO.getEmail()).build())
                .board(Board.builder().bno(replyDTO.getBno()).build())
                .build();

        return reply;
    }

    default ReplyDTO entityToDTO(Reply reply){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .email(reply.getMember().getEmail())
                .memberName(reply.getMember().getMemberName())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .bno(reply.getBoard().getBno())
                .build();

        return replyDTO;
    }


}
