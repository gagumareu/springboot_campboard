package coke.controller.camp.repository.Search;

import coke.controller.camp.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearchRepository {

    Page<Object[]> getSearchList(String type, String keyword, Pageable pageable, String category);
}
