package com.oheat.food.repository;

import static com.oheat.food.entity.QShopJpaEntity.shopJpaEntity;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class ShopCustomRepositoryImpl implements ShopCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ShopJpaEntity> findShopByCategory(CategoryJpaEntity category, Pageable pageable) {

        OrderSpecifier[] orders = createOrderSpecifier(pageable.getSort());

        List<ShopJpaEntity> content = jpaQueryFactory
            .selectFrom(shopJpaEntity)
            .where(shopJpaEntity.category.eq(category))
            .orderBy(orders)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(shopJpaEntity.count())
            .from(shopJpaEntity)
            .where(shopJpaEntity.category.eq(category))
            .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier[] createOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.getDirection().isDescending() ? Order.DESC : Order.ASC;
            String property = order.getProperty();
            switch (property) {
                case "id":
                    orders.add(new OrderSpecifier(direction, shopJpaEntity.id));
                case "minimumOrderAmount":
                    orders.add(new OrderSpecifier(direction, shopJpaEntity.minimumOrderAmount));
            }
        }
        return orders.toArray(OrderSpecifier[]::new);
    }
}
