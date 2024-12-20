package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.OptionJpaEntity;
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
@EqualsAndHashCode(of = {"id", "cartOptionGroup", "option"}, callSuper = false)
@Entity
@Table(name = "cart_option_group_option")
public class CartOptionGroupOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_option_group_id", nullable = false)
    private CartOptionGroup cartOptionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private OptionJpaEntity option;

    @Builder
    public CartOptionGroupOption(CartOptionGroup cartOptionGroup, OptionJpaEntity option) {
        this.cartOptionGroup = cartOptionGroup;
        this.option = option;
    }

    public void setCartOptionGroup(CartOptionGroup cartOptionGroup) {
        this.cartOptionGroup = cartOptionGroup;
    }
}
