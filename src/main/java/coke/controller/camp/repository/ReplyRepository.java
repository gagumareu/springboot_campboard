package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteByBno(@Param("bno") Long bno);

    List<Reply> getRepliesByBoardOrderByRnoDesc(Board board);

    List<Reply> getRepliesByMemberOrderByRnoDesc(Member member);

    @Query("SELECT r, m FROM Reply r LEFT JOIN r.member m WHERE r.board.bno = :bno")
    List<Object[]> getRepliesByBno(Long bno);

}
