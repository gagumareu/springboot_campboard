package coke.controller.camp.repository;

import coke.controller.camp.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Modifying
    @Query("DELETE FROM BoardImage bi WHERE bi.board.bno = :bno")
    void deleteByBno(Long bno);

    @Query("SELECT bi FROM BoardImage bi WHERE bi.board.bno = :bno")
    List<BoardImage> getBoardImagesByBno(Long bno);

}
