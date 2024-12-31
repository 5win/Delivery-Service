package com.oheat.order.repository;

import static com.oheat.order.entity.QOrder.order;

import com.oheat.order.entity.Order;
import com.oheat.user.entity.UserJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> findByUser(UserJpaEntity user, Pageable pageable) {

        List<Order> content = jpaQueryFactory
            .selectFrom(order)
            .where(order.user.eq(user))
            .orderBy(order.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(order.count())
            .from(order)
            .where(order.user.eq(user))
            .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
