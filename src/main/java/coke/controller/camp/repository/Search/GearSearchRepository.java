package coke.controller.camp.repository.Search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GearSearchRepository {

    Page<Object[]> getGearListWithSearching(String email, String type, String keyword, Pageable pageable);
}
