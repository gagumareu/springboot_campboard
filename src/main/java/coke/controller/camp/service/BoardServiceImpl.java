package coke.controller.camp.service;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.BoardImageRepository;
import coke.controller.camp.repository.BoardRepository;
import coke.controller.camp.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    @Override
    public Long register(BoardDTO boardDTO) {

        log.info("---------register-------");

        Map<String, Object> map = dtoToEntity(boardDTO);

        Board board = (Board) map.get("board");

        List<BoardImage> boardImageList = (List<BoardImage>) map.get("boardImage");

        log.info(board);
        log.info(boardImageList);

        boardRepository.save(board);

        if (boardImageList != null && boardImageList.size() > 0){
            boardImageList.forEach(boardImage -> {
                boardImageRepository.save(boardImage);
            });
        }

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getListWithImageMemberAndReplyCnt(PageRequestDTO pageRequestDTO) {

        log.info("---------getList-------");

        Function<Object[], BoardDTO> fn = (en -> entityToDTO(
                (Board) en[0],
                (List<BoardImage>) (Arrays.asList((BoardImage)en[1])),
                (Member) en[2],
                (Long) en[3]));

//        Page<Object[]> result = boardRepository.getListWithMemberAndReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = boardRepository.getSearchList(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()),
                pageRequestDTO.getCategory()
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO getBoardWithImagesMemberAndReplies(Long bno) {

        log.info("---------get-------");

        List<Object[]> result = boardRepository.getBoardWithAll(bno);

        Board board = (Board) result.get(0)[0];

        List<BoardImage> boardImageDTOList = new ArrayList<>();

        result.forEach(arr -> {
            BoardImage boardImage = (BoardImage) arr[1];
            boardImageDTOList.add(boardImage);
        });

        Member member = (Member) result.get(0)[2];

        Long replyCount = (Long) result.get(0)[3];

        log.info("board: " + board);
        log.info("image size: " + boardImageDTOList.size());
        log.info("member: " + member);
        log.info("reply counting : " + replyCount);

        return entityToDTO(board, boardImageDTOList, member, replyCount);
    }

    @Transactional
    @Override
    public void remove(Long bno) {

        log.info("---------remove-------");
        log.info("bno: " + bno);

        replyRepository.deleteByBno(bno);
        boardImageRepository.deleteByBno(bno);
        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        log.info("---------modify-------");

        boardImageRepository.deleteByBno(boardDTO.getBno());

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.get();

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        board.changeCategory(boardDTO.getCategory());

        boardRepository.save(board);

        List<BoardImage> boardImageList = boardImageDTOToEntity(boardDTO);

        if (boardImageList.size() > 0 && boardImageList != null){
            boardImageList.forEach(boardImage -> {
                boardImageRepository.save(boardImage);
            });
        }
    }

    @Override
    public List<BoardImageDTO> getBoardImageList(Long bno) {

        log.info("---------getBoardImageList via entityToDTO-----------");

        List<BoardImage> result = boardImageRepository.getBoardImagesByBno(bno);

        return imageEntityToDTO(result);
    }


}
