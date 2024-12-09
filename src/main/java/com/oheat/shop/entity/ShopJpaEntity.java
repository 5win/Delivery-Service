package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "shop")
public class ShopJpaEntity extends BaseTimeEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "name", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "minimum_order_amount", nullable = false)
    private int minimumOrderAmount;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private final List<MenuJpaEntity> menuList = new ArrayList<>();
}
