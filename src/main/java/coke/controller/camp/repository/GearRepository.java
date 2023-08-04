package coke.controller.camp.repository;

import coke.controller.camp.entity.Gear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GearRepository extends JpaRepository<Gear, Long> {

    @Query("SELECT g, m, gi " +
            "FROM Gear g " +
            "LEFT OUTER JOIN g.member m " +
            "LEFT OUTER JOIN GearImage gi ON gi.gear = g " +
            "WHERE g.member.email = :email group by g ORDER BY g.gno DESC ")
    List<Object[]> getGearByEmailOrderByGnoDesc(@Param("email") String email);
}
