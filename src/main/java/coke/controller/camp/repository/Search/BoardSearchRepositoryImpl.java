package coke.controller.camp.repository.Search;

import coke.controller.camp.entity.*;
import coke.controller.camp.entity.QBoard;
import coke.controller.camp.entity.QBoardImage;
import coke.controller.camp.entity.QMember;
import coke.controller.camp.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BoardSearchRepositoryImpl extends QuerydslRepositorySupport implements BoardSearchRepository{

    public BoardSearchRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search() {

        log.info("search.....!");

        return null;
    }

    @Override
    public Page<Object[]> getSearchList(String type, String keyword, Pageable pageable, String category) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;
        QBoardImage boardImage = QBoardImage.boardImage;
        QGear gear = QGear.gear;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.member.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        jpqlQuery.leftJoin(boardImage).on(boardImage.board.eq(board));
        jpqlQuery.leftJoin(gear).on(gear.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, boardImage, member, reply.countDistinct());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);
        booleanBuilder.and(expression);

        if (category != null){
            BooleanExpression expression1 = board.category.eq(category);
            booleanBuilder.and(expression1);
        }

        if (type != null){
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String typeValue : typeArr){
                switch (typeValue){
                    case "t" : conditionBuilder.or(board.title.contains(keyword));
                    break;
                    case "c" : conditionBuilder.or(board.content.contains(keyword));
                    break;
                    case "w" : conditionBuilder.or(member.memberName.contains(keyword));
                    break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderExpression.get(prop)));
        });

        tuple.groupBy(board);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info("result: " + result);

        Long count = tuple.fetchCount();

        log.info("count: " + count);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
