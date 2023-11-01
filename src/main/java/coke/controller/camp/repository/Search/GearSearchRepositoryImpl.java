package coke.controller.camp.repository.Search;

import coke.controller.camp.entity.*;
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
public class GearSearchRepositoryImpl extends QuerydslRepositorySupport implements GearSearchRepository{

    public GearSearchRepositoryImpl() {
        super(Gear.class);
    }

    @Override
    public Page<Object[]> getGearListWithSearching(String email, String sortType, String direction, String keyword, Pageable pageable) {

        log.info("-------gear search repository impl----------");
        log.info(email);
        log.info(sortType);
        log.info(direction);
        log.info(keyword);
        log.info(pageable);

        QGear gear = QGear.gear;
        QGearImage gearImage = QGearImage.gearImage;
        QMember member = QMember.member;
        QBoard board = QBoard.board;


        JPQLQuery<Gear> query = from(gear);
        query.leftJoin(member).on(gear.member.eq(member));
        query.leftJoin(gearImage).on(gearImage.gear.eq(gear));
        query.leftJoin(board).on(gear.board.eq(board));

        JPQLQuery<Tuple> tuple = query.select(gear, member, gearImage, board);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression expression = gear.gno.gt(0L);
        booleanBuilder.and(expression);

        BooleanExpression emailExpression = gear.member.email.eq(email);
        booleanBuilder.and(emailExpression);

        if (keyword ==null || keyword == "" || keyword.isEmpty()){
            keyword = "";
        }

        if (keyword.equals("null")){
            log.info("keyword is null");
        }else {
            log.info("keyword is available");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder.or(gear.gname.contains(keyword));
//            conditionBuilder.or(gear.brand.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            switch (property){
                case "email":
                    tuple.orderBy(new OrderSpecifier(orderDirection, member.email));
                    break;
                case "sort":
                    tuple.orderBy(new OrderSpecifier(orderDirection, gear.sort));
                    break;
                case "gname":
                    tuple.orderBy(new OrderSpecifier(orderDirection, gear.gname));
                    break;
                case "gno":
                    tuple.orderBy(new OrderSpecifier(orderDirection, gear.gno));
                    break;
                case "state":
                    tuple.orderBy(new OrderSpecifier(orderDirection, gear.state));
            }

//            PathBuilder oderByExpression = new PathBuilder(Gear.class, "gear");
//            tuple.orderBy(new OrderSpecifier(orderDirection, oderByExpression.get(property)));
        });

        log.info(sort);

        tuple.groupBy(gear);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        Long count = tuple.fetchCount();

        log.info(count);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }


}
