package coke.controller.camp.service;

import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardImageServiceImpl implements BoardImageService{

    private final BoardImageRepository boardImageRepository;


    @Override
    public List<BoardImageDTO> getImageList(Long bno) {

        log.info("-------getImageList-------------");

        List<BoardImage> boardImageList = boardImageRepository.getBoardImagesByBno(bno);

        return entityToDTO(boardImageList);
    }
}
