package coke.controller.camp.repository.Search;

import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.QGear;
import coke.controller.camp.entity.QGearImage;
import coke.controller.camp.entity.QMember;
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
    public Page<Object[]> getGearListWithSearching(String email, String type, String keyword, Pageable pageable) {

        QGear gear = QGear.gear;
        QGearImage gearImage = QGearImage.gearImage;
        QMember member = QMember.member;

        JPQLQuery<Gear> query = from(gear);
        query.leftJoin(member).on(gear.member.eq(member));
        query.leftJoin(gearImage).on(gearImage.gear.eq(gear));

        JPQLQuery<Tuple> tuple = query.select(gear, member, gearImage);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = gear.gno.gt(0L);

        booleanBuilder.and(expression);

        BooleanExpression emailExpression = gear.member.email.eq(email);
        booleanBuilder.and(emailExpression);

        if ((type != null && type.length() > 0) && keyword != null){

            String[] typeArr = type.split("");

            BooleanBuilder conditionBuilder = new BooleanBuilder();

            // n = 기어이름 b= 브랜드명 s = 카테고리(sort)
            for (String types: typeArr){
                switch (types){
                    case "n":
                        conditionBuilder.or(gear.gname.contains(keyword));
                        break;
                    case "b":
                        conditionBuilder.or(gear.brand.contains(keyword));
                        break;
                    case "s":
                        conditionBuilder.or(gear.sort.contains(keyword));
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder oderByExpression = new PathBuilder(Gear.class, "gear");
            tuple.orderBy(new OrderSpecifier(direction, oderByExpression.get(property)));
        });

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
