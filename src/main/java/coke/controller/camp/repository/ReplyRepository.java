package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void removeAllByBno(Long bno);

    List<Reply> getRepliesByBoardOrderByRnoDesc(Board board);

}
