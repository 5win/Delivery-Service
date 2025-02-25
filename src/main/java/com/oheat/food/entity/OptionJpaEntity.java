package com.oheat.food.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.dto.OptionUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
@Entity
@Table(name = "options")
public class OptionJpaEntity extends BaseTimeEntity {

    @Builder
    public OptionJpaEntity(String name, int price, OptionGroupJpaEntity optionGroup) {
        this.name = name;
        this.price = price;
        this.optionGroup = optionGroup;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    private OptionGroupJpaEntity optionGroup;

    public void updateOptionInfo(OptionUpdateRequest updateRequest) {
        this.name = updateRequest.getName();
        this.price = updateRequest.getPrice();
    }
}
