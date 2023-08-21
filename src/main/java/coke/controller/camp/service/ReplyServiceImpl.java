package coke.controller.camp.service;

import coke.controller.camp.dto.ReplyDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.Reply;
import coke.controller.camp.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;


    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = dtoToEntity(replyDTO);

        Reply result = replyRepository.save(reply);

        return result.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {

        Board board = Board.builder().bno(bno).build();

        List<Reply> result = replyRepository.getRepliesByBoardOrderByRnoDesc(board);

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public ReplyDTO get(Long rno) {

        Optional<Reply> result = replyRepository.findById(rno);

        Reply reply = result.get();

        return entityToDTO(reply);
    }

    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);

    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

    }

    @Override
    public List<ReplyDTO> getListByEmail(String email) {

        Member member = Member.builder().email(email).build();

        List<Reply> result = replyRepository.getRepliesByMemberOrderByRnoDesc(member);

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }
}
