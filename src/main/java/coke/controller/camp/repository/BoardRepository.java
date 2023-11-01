package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.Search.BoardSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearchRepository {

    @Query(value = "SELECT b, bi, m, count (distinct r) " +
            "FROM Board b " +
            "LEFT OUTER JOIN b.member m " +
            "LEFT OUTER JOIN Reply r ON r.board = b " +
            "LEFT OUTER JOIN BoardImage bi ON bi.board = b " +
            "GROUP BY b",
            countQuery = "SELECT count (b) FROM Board b")
    Page<Object[]> getListWithMemberAndReplyCount(Pageable pageable);

    @Query("SELECT b, bi, m, count (distinct r), gi " +
            "FROM Board b " +
            "LEFT OUTER JOIN b.member m " +
            "LEFT OUTER JOIN Reply r ON r.board = b " +
            "LEFT OUTER JOIN BoardImage bi ON bi.board = b " +
            "LEFT OUTER JOIN Gear g ON g.member = m " +
            "LEFT OUTER JOIN GearImage gi ON gi.gear = g " +
            "WHERE b.bno = :bno " +
            "GROUP BY bi")
    List<Object[]> getBoardWithAll(@Param("bno") Long bno);

    @Query("SELECT b FROM Board b WHERE b.member.email = :email ORDER BY b.bno DESC ")
    List<Board> getBoardByEmail(String email);

    List<Board> getBoardsByMemberOrderByBnoDesc(Member member);

    @Query("SELECT b, bi FROM Board b LEFT JOIN BoardImage bi on bi.board = b WHERE b.category = 'talk' order by b.bno desc limit 5")
    List<Object[]> getBoardListByCategoryTalkLimit();

    @Query("SELECT b, gi FROM Board b LEFT JOIN Gear g on g.board = b LEFT JOIN GearImage gi ON gi.gear = g WHERE b.category = 'secondHands' order by b.bno desc limit 5")
    List<Object[]> getBoardListByCategorySecondHansLimit();

}
