package coke.controller.camp.repository;

import coke.controller.camp.entity.Gear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearRepository extends JpaRepository<Gear, Long> {
}
