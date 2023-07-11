package coke.controller.camp.service;

import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.entity.BoardImage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardImageService {

    List<BoardImageDTO> getImageList(Long bno);

    default List<BoardImageDTO> entityToDTO(List<BoardImage> boardImageList) {

        List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

        if (boardImageList != null && boardImageList.size() > 0) {
            boardImageDTOList = boardImageList.stream().map(boardImage -> {
                if (boardImage != null) {
                    return BoardImageDTO.builder()
                            .folderPath(boardImage.getFolderPath())
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .build();
                } else {
                    return null;
                }
            }).collect(Collectors.toList());

        }
        return boardImageDTOList;
    }

}
