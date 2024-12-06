package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name = "shops")
public class ShopJpaEntity extends BaseTimeEntity {

    @Column(name = "shop_name", nullable = false, unique = true)
    private String shopName;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "category", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "minimum_order_amount", nullable = false)
    private int minimumOrderAmount;
}
