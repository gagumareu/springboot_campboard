package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.repository.Search.GearSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GearRepository extends JpaRepository<Gear, Long>, GearSearchRepository {

    @Query("SELECT g, m, gi, b " +
            "FROM Gear g " +
            "LEFT OUTER JOIN g.member m " +
            "LEFT OUTER JOIN GearImage gi ON gi.gear = g left JOIN g.board b WHERE g.member.email = :email group by g ORDER BY g.gno DESC ")
    List<Object[]> getGearByEmailOrderByGnoDesc(@Param("email") String email);

    @Query("SELECT g, m, gi, b FROM Gear g LEFT JOIN g.member m LEFT JOIN GearImage gi ON gi.gear = g LEFT JOIN g.board b WHERE g.gno = :gno")
    List<Object[]> getGearByGno(@Param("gno") Long gno);

    @Query("SELECT g, m, gi " +
            "FROM Gear g " +
            "LEFT OUTER JOIN g.member m " +
            "LEFT OUTER JOIN GearImage gi ON gi.gear = g " +
            "WHERE g.member.email = :email " +
            "GROUP BY g order by g.gno DESC ")
    Page<Object[]> getGearListForPagination(String email, Pageable pageable);

    Optional<Gear> getGearByBoard(Board board);


}
