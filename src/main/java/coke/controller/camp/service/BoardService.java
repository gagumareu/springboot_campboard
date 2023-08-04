package coke.controller.camp.service;

import coke.controller.camp.dto.BoardDTO;
import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.entity.Member;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    PageResultDTO<BoardDTO, Object[]> getListWithImageMemberAndReplyCnt(PageRequestDTO pageRequestDTO);
    BoardDTO getBoardWithImagesMemberAndReplies(Long bno);
    void remove(Long bno);
    void modify(BoardDTO boardDTO);
    List<BoardImageDTO> getBoardImageList(Long bno);

    default Map<String, Object> dtoToEntity(BoardDTO boardDTO){

        Map<String, Object> map = new HashMap<>();

        Member member = Member.builder().email(boardDTO.getEmail()).build();

        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .category(boardDTO.getCategory())
                .member(member)
                .build();
        map.put("board", board);

        List<BoardImageDTO> boardImageDTOList = boardDTO.getBoardImageDTOList();

        if(boardImageDTOList.size() > 0 && boardImageDTOList != null){
            List<BoardImage> boardImageList = boardImageDTOList.stream().map(boardImageDTO -> {
                BoardImage boardImage = BoardImage.builder()
                        .folderPath(boardImageDTO.getFolderPath())
                        .uuid(boardImageDTO.getUuid())
                        .fileName(boardImageDTO.getFileName())
                        .board(board)
                        .build();
                return boardImage;
            }).collect(Collectors.toList());
            map.put("boardImage", boardImageList);
        }
        return map;

    } // dtoToEntity

    default BoardDTO entityToDTO(Board board, List<BoardImage> boardImageList, Member member, Long replyCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .email(member.getEmail())
                .category(board.getCategory())
                .memberName(member.getMemberName())
                .replyCount(replyCount.intValue())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        if (boardImageList != null && boardImageList.size() > 0){
            List<BoardImageDTO> boardImageDTOList = boardImageList.stream().map(boardImage -> {
                if (boardImage != null){
                    return BoardImageDTO.builder()
                            .folderPath(boardImage.getFolderPath())
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .build();
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            boardDTO.setBoardImageDTOList(boardImageDTOList);
        }
        return boardDTO;

    } // entityToDTO

    default List<BoardImage> boardImageDTOToEntity(BoardDTO boardDTO){

        Board board = Board.builder().bno(boardDTO.getBno()).build();

        List<BoardImageDTO> boardImageDTOList = boardDTO.getBoardImageDTOList();

        List<BoardImage> boardImageList = boardImageDTOList.stream().map(boardImageDTO -> {
            BoardImage boardImage = BoardImage.builder()
                    .folderPath(boardImageDTO.getFolderPath())
                    .uuid(boardImageDTO.getUuid())
                    .fileName(boardImageDTO.getFileName())
                    .board(board)
                    .build();
            return boardImage;
        }).collect(Collectors.toList());

        return boardImageList;
    }

    default List<BoardImageDTO> imageEntityToDTO(List<BoardImage> boardImageList){

        List<BoardImageDTO> boardImageDTOList = boardImageList.stream().map(boardImage -> {
            if (boardImage != null){
                return BoardImageDTO.builder()
                        .folderPath(boardImage.getFolderPath())
                        .uuid(boardImage.getUuid())
                        .fileName(boardImage.getFileName())
                        .build();
            }else {
                return null;
            }
        }).collect(Collectors.toList());
        return boardImageDTOList;

    }


}
