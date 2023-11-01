package coke.controller.camp.service;

import coke.controller.camp.dto.*;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.entity.GearImage;
import coke.controller.camp.entity.Member;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
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
    List<BoardDTO> getBoardByEmail(String email);
    List<BoardDTO> getBoardByTalkCategoryLimit();
    List<BoardDTO> getBoardBySecondHandsCategoryLimit();

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
                        .s3Url(boardImageDTO.getS3Url())
                        .board(board)
                        .build();
                return boardImage;
            }).collect(Collectors.toList());
            map.put("boardImage", boardImageList);
        }
        return map;

    } // dtoToEntity

    default BoardDTO entityToDTO(Board board, List<BoardImage> boardImageList, Member member, Long replyCount, List<GearImage> gearImages){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .email(member.getEmail())
                .category(board.getCategory())
                .memberName(member.getMemberName())
                .profileImg(member.getProfileImg())
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
                            .s3Url(boardImage.getS3Url())
                            .build();
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            boardDTO.setBoardImageDTOList(boardImageDTOList);
        }

        if (gearImages != null && gearImages.size() > 0){
            List<GearImageDTO> gearImageDTOList = gearImages.stream().map(gearImage -> {
                if (gearImage != null){
                    return GearImageDTO.builder()
                            .folderPath(gearImage.getFolderPath())
                            .uuid(gearImage.getUuid())
                            .fileName(gearImage.getFileName())
                            .s3Url(gearImage.getS3Url())
                            .build();
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            boardDTO.setGearImageDTOList(gearImageDTOList);
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
                    .s3Url(boardImageDTO.getS3Url())
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
                        .s3Url(boardImage.getS3Url())
                        .build();
            }else {
                return null;
            }
        }).collect(Collectors.toList());
        return boardImageDTOList;

    }

    default BoardDTO basicEntityToDTO(Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
        return boardDTO;
    }

    default BoardDTO entityBoardNBoardImgToDTO(Board board, BoardImage boardImage){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

        if (boardImage != null){
            BoardImageDTO boardImageDTO = BoardImageDTO.builder()
                    .uuid(boardImage.getUuid())
                    .folderPath(boardImage.getFolderPath())
                    .fileName(boardImage.getFileName())
                    .s3Url(boardImage.getS3Url())
                    .build();
            boardImageDTOList.add(boardImageDTO);
        }

        boardDTO.setBoardImageDTOList(boardImageDTOList);

        return boardDTO;
    }


    default BoardDTO entityBoardNGearImgToDTO(Board board, GearImage gearImage){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<GearImageDTO> gearImageDTOList = new ArrayList<>();

        if (gearImage != null){
            GearImageDTO gearImageDTO = GearImageDTO.builder()
                    .s3Url(gearImage.getS3Url())
                    .folderPath(gearImage.getFolderPath())
                    .fileName(gearImage.getFileName())
                    .uuid(gearImage.getUuid())
                    .build();
            gearImageDTOList.add(gearImageDTO);
        }

        boardDTO.setGearImageDTOList(gearImageDTOList);

        return boardDTO;
    }



}
