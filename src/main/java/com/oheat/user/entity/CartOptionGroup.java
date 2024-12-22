package com.oheat.user.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "cart", "optionGroup"}, callSuper = false)
@Entity
@Table(name = "cart_option_group")
public class CartOptionGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartJpaEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    private OptionGroupJpaEntity optionGroup;

    @OneToMany(mappedBy = "cartOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartOptionGroupOption> cartOptionGroupOptions = new ArrayList<>();

    @Builder
    public CartOptionGroup(CartJpaEntity cart, OptionGroupJpaEntity optionGroup) {
        this.cart = cart;
        this.optionGroup = optionGroup;
    }

    public void setCart(CartJpaEntity cart) {
        this.cart = cart;
    }

    public void addCartOption(CartOptionGroupOption option) {
        option.setCartOptionGroup(this);
        this.cartOptionGroupOptions.add((option));
    }

    public int calcTotalPriceOption() {
        return this.cartOptionGroupOptions.stream()
            .mapToInt(o -> o.getOption().getPrice())
            .sum();
    }
}
