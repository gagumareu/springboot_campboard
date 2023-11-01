package coke.controller.camp.repository;

import coke.controller.camp.dto.PartyDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Party;
import coke.controller.camp.repository.Search.PartySearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long>, PartySearchRepository {

    @Query("SELECT p, m, g, gi " +
            "FROM Party p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN Gear g ON g.member = m " +
            "LEFT JOIN GearImage gi ON gi.gear = g " +
            "WHERE p.board.bno = :bno " +
            "group by g")
    List<Object[]> getPartyByBno(Long bno);

    @Query("select count (p) FROM Party p WHERE p.board.bno = :bno AND p.member.email = :email")
    int getPartyByBnoAndEmail(Long bno, String email);

    @Modifying
    @Query("DELETE FROM Party p WHERE p.board.bno = :bno AND p.member.email = :email")
    int dropOutFromParty(Long bno, String email);

    @Query("SELECT distinct (p.location) FROM Party p WHERE p.board.bno = :bno")
    String getLocationByBno(Long bno);

    @Modifying
    @Query("DELETE FROM Party p WHERE p.board.bno = :bno")
    void deletePartiesByBno(Long bno);

    @Query("SELECT p FROM Party p WHERE p.board.bno = :bno")
    List<Party> getPartiesByBno(Long bno);

    @Query("SELECT p , m FROM Party p LEFT JOIN p.member m WHERE p.board.bno = :bno")
    List<Object[]> getApplicantsByBno(Long bno);

    @Query("SELECT count (p.board.bno) FROM Party p where p.board.bno = :bno")
    Long getPartyCountingApplicantByBno(Long bno);

    @Query("SELECT p FROM Party p WHERE p.board.bno = :bno")
    Party getParty(Long bno);

    @Query("SELECT DISTINCT(p.board.bno), p, b FROM Party p LEFT JOIN p.board b WHERE p.member.email = :email ORDER BY p.appointment ASC ")
    List<Object[]> getPartiesBoardListByEmail(String email);

    @Query("SELECT DISTINCT(p.board.bno), p, b " +
            "FROM Party p LEFT JOIN p.board b " +
            "WHERE p.member.email = :email " +
            "AND p.appointment BETWEEN :start AND :end GROUP BY b " +
            "ORDER BY p.appointment DESC ")
    List<Object[]> getPartiesBoardListByDateRangeNEmail(LocalDate start, LocalDate end, String email);

    // 기간 내 캠핑 모임 게시물 불러오기
    @Query("SELECT p, b.bno, b.title, b.category, bi.s3Url " +
            "FROM Party p " +
            "LEFT JOIN p.board b LEFT JOIN BoardImage bi ON bi.board = b " +
            "WHERE p.appointment BETWEEN :start AND :end " +
            "GROUP BY b.bno order by p.appointment ASC ")
    List<Object[]> getPartiesAllListWithRange(LocalDate start, LocalDate end);

    @Query("SELECT p FROM Party p WHERE p.board.bno = :bno GROUP BY p.board.bno")
    Party getPartyInfoByBno(Long bno);

}
