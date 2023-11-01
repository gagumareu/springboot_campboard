package coke.controller.camp.repository.Search;

import coke.controller.camp.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
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
public class PartySearchRepositoryImpl extends QuerydslRepositorySupport implements PartySearchRepository {

    public PartySearchRepositoryImpl() {
        super(Party.class);
    }

    @Override
    public Page<Object[]> getPartyMemberWithGears(Long bno, String direction, String sortType, String keyword, Pageable pageable) {

        log.info("-------------getPartyMemberWithGears-------------");
        log.info(bno);
        log.info("sortType: " + sortType);
        log.info("direction: " + direction);
        log.info("keyword: " + keyword);
        log.info(pageable);

        QParty party = QParty.party;
        QMember member = QMember.member;
        QBoard board = QBoard.board;
        QGear gear = QGear.gear;
        QGearImage gearImage = QGearImage.gearImage;

        JPQLQuery<Party> query = from(party);
        query.leftJoin(member).on(party.member.eq(member));
        query.leftJoin(board).on(party.board.eq(board));
        query.leftJoin(gear).on(gear.member.eq(member));
        query.leftJoin(gearImage).on(gearImage.gear.eq(gear));

        JPQLQuery<Tuple> tuple = query.select(party, member, board, gear, gearImage);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression bnoExpression = party.board.bno.eq(bno);
        booleanBuilder.and(bnoExpression);

        if (keyword == null || keyword == "" || keyword.isEmpty()){
            keyword = "";
        }

        log.info(keyword);

        if ( keyword.equals("null")){
            log.info("********************keyword is null********************");
        }else {
            log.info("********************keyword is not null********************");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder.or(gear.gname.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }


        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            log.info("property: " + property);
            log.info("sort: " + sort);

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
            }
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

    @Override
    public Page<Object[]> getPartyGearList(Long bno, String direction, String sortType, String keyword, Pageable pageable) {

        log.info("-------------getPartyMemberWithGears-------------");
        log.info(bno);
        log.info("sortType: " + sortType);
        log.info("direction: " + direction);
        log.info("keyword: " + keyword);
        log.info(pageable);

        QPartyGear partyGear = QPartyGear.partyGear;
        QGear gear = QGear.gear;
        QGearImage gearImage = QGearImage.gearImage;
        QMember member = QMember.member;
//        QBoard board = QBoard.board;

        JPQLQuery<PartyGear> query = from(partyGear);
//        query.leftJoin(board).on(partyGear.board.eq(board));
        query.leftJoin(member).on(member.email.eq(gear.member.email));
        query.leftJoin(gear).on(partyGear.gear.eq(gear));
        query.leftJoin(gearImage).on(gearImage.gear.eq(gear));

        JPQLQuery<Tuple> tuple = query.select(partyGear, member, gear, gearImage );

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression bnoExpression = partyGear.board.bno.eq(bno);
        booleanBuilder.and(bnoExpression);

        if (keyword == null || keyword == "" || keyword.isEmpty()){
            keyword = "";
        }

        log.info(keyword);

        if ( keyword.equals("null")){
            log.info("********************keyword is null********************");
        }else {
            log.info("********************keyword is not null********************");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder.or(gear.gname.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            log.info("property: " + property);
            log.info("sort: " + sort);

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
            }
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
