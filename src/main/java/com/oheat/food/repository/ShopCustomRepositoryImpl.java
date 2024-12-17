package com.oheat.food.repository;

import static com.oheat.food.entity.QShopJpaEntity.shopJpaEntity;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ShopCustomRepositoryImpl implements ShopCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ShopJpaEntity> findShopByCategory(CategoryJpaEntity category, Pageable pageable) {

        List<ShopJpaEntity> content = jpaQueryFactory
            .selectFrom(shopJpaEntity)
            .where(shopJpaEntity.category.eq(category))
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
}
