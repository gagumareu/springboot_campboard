package coke.controller.camp.repository.BoardSearch;

import coke.controller.camp.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearchRepository {

    Board search();

    Page<Object[]> getSearchList(String type, String keyword, Pageable pageable, String category);
}
