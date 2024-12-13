package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"price"}, callSuper = false)
@Entity
@Table(name = "option")
public class OptionJpaEntity extends BaseTimeEntity {

    @Builder
    public OptionJpaEntity(String name, int price, Long optionGroupId) {
        this.name = name;
        this.price = price;
        this.optionGroupId = optionGroupId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "option_group_id", nullable = false)
    private Long optionGroupId;
}
