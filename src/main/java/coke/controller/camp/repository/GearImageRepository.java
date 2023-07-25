package coke.controller.camp.repository;

import coke.controller.camp.entity.GearImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GearImageRepository extends JpaRepository<GearImage, Long> {

    @Modifying
    @Query("DELETE FROM GearImage gi WHERE gi.gear.gno = :gno")
    void removeByGno(@Param("gno") Long gno);

    @Query("SELECT gi FROM GearImage gi WHERE gi.gear.gno = :gno")
    List<GearImage> getGearImagesByGno(@Param("gno") Long gno);
}
